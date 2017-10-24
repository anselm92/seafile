package com.bwksoftware.android.seasync.data.service

import android.accounts.Account
import android.accounts.AccountManager
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import android.util.Log
import com.bwksoftware.android.seasync.data.datamanager.StorageManager
import com.bwksoftware.android.seasync.data.entity.Item
import com.bwksoftware.android.seasync.data.entity.Repo
import com.bwksoftware.android.seasync.data.net.RestApiImpl
import com.bwksoftware.android.seasync.data.prefs.SharedPrefsController
import com.bwksoftware.android.seasync.data.provider.FileRepoContract
import javax.inject.Inject


class FileSyncAdapter constructor(val mContext: Context) : AbstractThreadedSyncAdapter(
        mContext, true) {

    @Inject
    lateinit var restApi: RestApiImpl

    @Inject
    lateinit var prefs: SharedPrefsController

    @Inject
    lateinit var storageManager: StorageManager

    private val accountManager = AccountManager.get(mContext)

    override fun onPerformSync(p0: Account?, p1: Bundle?, p2: String?, p3: ContentProviderClient?,
                               p4: SyncResult?) {
        Log.d("FileSyncService", "onPerformSync")

        val authToken = accountManager.blockingGetAuthToken(p0, "full_access",
                true)

        // retrieve list of repos and compare their modified time to local repos from db
        val repos = restApi.getRepoListSync(authToken).execute().body()

        for (remoteRepo in repos!!) {
            val cursor = p3?.query(FileRepoContract.Repo.buildRepoUri(remoteRepo.id!!.toLong()),
                    FileRepoContract.Repo.PROJECTION_ALL, null,
                    null, null)
            // IF cursor is null there is no local repo, meaning that it's not synced.
            if (cursor != null) {
                val localRepo = storageManager.createRepoInstance(cursor)
                compareReposAndSync(p3, authToken, localRepo, remoteRepo)
            }
        }
        // if not equal retrieve list of entries for that repo and compare to local files
    }

    fun compareReposAndSync(contentProviderClient: ContentProviderClient, authToken: String,
                            localRepo: Repo, remoteRepo: Repo) {
        if (localRepo.mtime!! != remoteRepo.mtime!!) {
            // Retrieve list of content from server
            syncDirectoryRecursive(contentProviderClient, authToken, null, localRepo, "/")
        }
    }

    fun syncDirectoryRecursive(contentProviderClient: ContentProviderClient, authToken: String,
                               parent: Item?,
                               repo: Repo, path: String) {
        val localItemsForRepo = storageManager.getItemsForRepo(repo, "dir").toMutableList()
        val remoteItemsForRepo = restApi.getDirectoryEntriesSync(authToken, repo.id!!,
                path).execute().body()

        // If we're in the root directory we have to check if the repo should be synced
        // if we're in a folder we have to check if the parent folder was marked to be synced.
        val syncContents = if (parent == null) repo.fullSync!! else parent.synced!!

        for (remoteItem in remoteItemsForRepo!!) {
            //TODO: we need to remove every item we've checked from the localitem list so we can check
            //TODO if there are any items remaining which then have to be deleted as they no longer
            //TODO exist remotely
            val localItem = storageManager.getFile(repo.dbId!!, path, remoteItem.name!!)
            if (localItem != null) {
                // if the folder locally exists and is on remote
                // we need check its contents whether they are synced if their mtime differs
                if (localItem.type == "dir" && localItem.mtime != remoteItem.mtime) {
                    syncDirectoryRecursive(contentProviderClient, authToken, localItem, repo,
                            path + "/" + localItem.name)

                } else {
                    // if its a file and exists locally we have to compare the time stamps
                    if (localItem.mtime != remoteItem.mtime) {
                        storageManager.syncItem(authToken, repo, localItem, remoteItem)
                    }
                }
            } else if (syncContents) {
                // we don't have this file and all files of this folder should be synced
                // -> we need to download it
                // test if file is a dir or a file and sync
                if (remoteItem.type == "dir") {
                    storageManager.saveItemInstance(remoteItem)
                    syncDirectoryRecursive(contentProviderClient, authToken, remoteItem, repo,
                            path + "/" + remoteItem.name)
                } else {
                    storageManager.syncItem(authToken, repo, localItem, remoteItem)
                }

            } else {
                // Nothing, repo or directory are not marked to be synced, thus we ignore it
            }

        }
    }


    // TODO: create a synctask which downloads/uploads a file and creates/updates db entries accordingly
    // -

    //TODO: When a user selects a folder, repo or file to be synced, the ui has to
    // create all necessary db entries.
    // The syncservice will then be triggered and download things
}