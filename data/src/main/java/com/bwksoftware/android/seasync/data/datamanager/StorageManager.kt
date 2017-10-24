package com.bwksoftware.android.seasync.data.datamanager

import android.accounts.Account
import android.content.ContentProviderClient
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import com.bwksoftware.android.seasync.data.entity.Item
import com.bwksoftware.android.seasync.data.entity.Repo
import com.bwksoftware.android.seasync.data.net.RestApiImpl
import com.bwksoftware.android.seasync.data.provider.FileRepoContract
import com.bwksoftware.android.seasync.data.sync.SyncManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject


class StorageManager @Inject constructor(val mAccount: Account,
                                         val context: Context,
                                         val contentProviderClient: ContentProviderClient,
                                         val restApi: RestApiImpl) {

    fun createItemInstance(cursor: Cursor): Item {
        val item = Item()
        item.dbId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
        item.name = cursor.getString(cursor.getColumnIndex(FileRepoContract.FileColumns.NAME))
        item.mtime = cursor.getLong(cursor.getColumnIndex(FileRepoContract.FileColumns.MOD_DATE))
        item.size = cursor.getLong(cursor.getColumnIndex(FileRepoContract.FileColumns.SIZE))
        item.path = cursor.getString(cursor.getColumnIndex(FileRepoContract.FileColumns.PATH))
        item.type = cursor.getString(cursor.getColumnIndex(FileRepoContract.FileColumns.TYPE))
        item.id = cursor.getString(cursor.getColumnIndex(FileRepoContract.FileColumns.REMOTE_ID))
        item.storage = cursor.getString(cursor.getColumnIndex(FileRepoContract.FileColumns.STORAGE))
        item.synced = cursor.getInt(cursor.getColumnIndex(FileRepoContract.FileColumns.SYNCED)) == 1
        return item
    }


    fun saveItemInstance(item: Item) {
        val contentValues = ContentValues()
        contentValues.put(FileRepoContract.FileColumns.NAME, item.name)
        contentValues.put(FileRepoContract.FileColumns.SIZE, item.size)
        contentValues.put(FileRepoContract.FileColumns.MOD_DATE, item.mtime)
        contentValues.put(FileRepoContract.FileColumns.PATH, item.path)
        contentValues.put(FileRepoContract.FileColumns.TYPE, item.type)
        contentValues.put(FileRepoContract.FileColumns.REMOTE_ID, item.id)
        contentValues.put(FileRepoContract.FileColumns.SYNCED, item.synced)
        contentValues.put(FileRepoContract.FileColumns.STORAGE, item.storage)

        if (item.dbId != null) {
            val resultUri = contentProviderClient.update(FileRepoContract.File.buildFileUri(
                    item.dbId!!),
                    contentValues, null, null)
        } else {
            val resultUri = contentProviderClient.insert(FileRepoContract.File.CONTENT_URI,
                    contentValues)
            val id = resultUri.pathSegments[1]
            item.dbId = id.toLong()
        }
    }

    fun syncItem(authToken: String, repo: Repo, localItem: Item?, remoteItem: Item) {
        if (localItem == null) {
            downloadAndUpdateItem(authToken, repo, remoteItem, remoteItem)
        } else {
            if (localItem.mtime!! > remoteItem.mtime!!) {
                // our item is newer, upload it
                uploadAndUpdateItem(authToken, repo, localItem, remoteItem)
            } else {
                downloadAndUpdateItem(authToken, repo, localItem, remoteItem)
                // remote item is newer download it and update db
            }
        }
    }

    fun downloadAndUpdateItem(authToken: String, repo: Repo, localItem: Item, remoteItem: Item) {
        val call = restApi.getFileDownloadLink(authToken, remoteItem.path + "/" + remoteItem.name)
        call.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                val downloadCall = restApi.downloadFile(response!!.body()!!)
                downloadCall.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<ResponseBody>?,
                                            response: Response<ResponseBody>?) {
                        if (response != null) {
                            if (response.isSuccessful) {
                                val asyncTask = SyncManager.DownloadTask(localItem,
                                        response.body()!!)
                                asyncTask.execute()
                                localItem.mtime = remoteItem.mtime
                                localItem.size = remoteItem.size
                                saveItemInstance(localItem)
                            }
                        }
                    }
                })
            }
        })
    }


    fun uploadAndUpdateItem(authToken: String, repo: Repo, localItem: Item, remoteItem: Item) {
        val call = restApi.getUpdateLink(authToken, repo.id!!, remoteItem.path!!)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>?, response: Response<String>?) {
                val url = response!!.body()
                val directory = File(localItem.storage, localItem.name)
                val uploadCall = restApi.updateFile(url!!, authToken,
                        File(directory, localItem.name))
                uploadCall.enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>?, t: Throwable?) {

                    }

                    override fun onResponse(call: Call<String>?,
                                            response: Response<String>?) {
                        //Awesome..
//                        localItem.mtime = remoteItem.mtime
                        // todo we should retrieve the correct mtime from server
                        saveItemInstance(localItem)
                    }

                })
            }

            override fun onFailure(call: Call<String>?, t: Throwable?) {

            }

        })
    }

    fun createRepoInstance(cursor: Cursor): Repo {
        val repo = Repo()
        repo.dbId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
        repo.id = cursor.getInt(cursor.getColumnIndex(FileRepoContract.RepoColumns.HASH)).toString()
        repo.name = cursor.getString(cursor.getColumnIndex(FileRepoContract.RepoColumns.NAME))
        repo.mtime = cursor.getLong(cursor.getColumnIndex(FileRepoContract.RepoColumns.MOD_DATE))
        repo.fullSync = cursor.getInt(cursor.getColumnIndex(
                FileRepoContract.RepoColumns.FULL_SYNCED)) == 1
        return repo
    }

    fun getItemsForRepo(repo: Repo, path: String): List<Item> {
        val cursor = contentProviderClient.query(FileRepoContract.File.CONTENT_URI,
                null,
                FileRepoContract.FileColumns.REPO_ID + "=?",
                arrayOf(repo.dbId.toString()),
                null)
        val items = ArrayList<Item>()
        do {
            items.add(createItemInstance(cursor))
        } while (cursor.moveToNext())
        cursor.close()
        return items
    }

    fun saveRepoInstance(repo: Repo) {
        val contentValues = ContentValues()
        contentValues.put(FileRepoContract.RepoColumns.NAME, repo.name)
        contentValues.put(FileRepoContract.RepoColumns.MOD_DATE, repo.mtime)
        contentValues.put(FileRepoContract.RepoColumns.FULL_SYNCED, repo.fullSync)
        if (repo.dbId != null) {
            val resultUri = contentProviderClient.update(FileRepoContract.File.buildFileUri(
                    repo.dbId!!),
                    contentValues, null, null)
        } else {
            val resultUri = contentProviderClient.insert(FileRepoContract.File.CONTENT_URI,
                    contentValues)
            val id = resultUri.pathSegments[1]
            repo.dbId = id.toLong()
        }
    }

    fun getFile(repoId: Long, path: String, name: String): Item? {
        val cursor = contentProviderClient.query(FileRepoContract.File.CONTENT_URI,
                null,
                FileRepoContract.FileColumns.REPO_ID + "=? AND " +
                        FileRepoContract.FileColumns.PATH + "=? AND " +
                        FileRepoContract.FileColumns.NAME + "=?",
                arrayOf(repoId.toString(), path, name),
                null
        )
        var item: Item? = null
        if (cursor.moveToFirst())
            item = createItemInstance(cursor)
        cursor.close()
        return item
    }

    fun fileExists(path: String): Boolean {
        return fileExists(FileRepoContract.FileColumns.PATH, path)
    }

    fun fileExists(selectionKey: String, selectionValue: String): Boolean {
        val cursor = contentProviderClient.query(FileRepoContract.File.CONTENT_URI,
                null,
                selectionKey + "=?", arrayOf(selectionValue),
                FileRepoContract.File.SORT_ORDER_DEFAULT
        )
        val result = cursor.moveToFirst()
        cursor.close()
        return result
    }


}
