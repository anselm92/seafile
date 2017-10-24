package com.bwksoftware.android.seasync.data.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import com.bwksoftware.android.seasync.data.db.DBHelper
import javax.inject.Inject

class FileRepoContentProvider : ContentProvider() {

    @Inject lateinit var dbHelper: DBHelper
    private val uriMatcher = buildUriMatcher()

    override fun insert(uri: Uri?, contentValues: ContentValues?): Uri {
        val db = dbHelper.writableDatabase
        val id: Long
        val returnUri: Uri
        when (uriMatcher.match(uri)) {
            REPO -> {
                id = db.insert(FileRepoContract.RepoColumns.TABLE_NAME, null, contentValues)
                if (id > 0)
                    returnUri = FileRepoContract.Repo.buildRepoUri(id)
                else throw UnsupportedOperationException("Failed to insert rows :" + contentValues) as Throwable
            }
            FILE -> {
                id = db.insert(FileRepoContract.FileColumns.TABLE_NAME, null, contentValues)
                if (id > 0)
                    returnUri = FileRepoContract.File.buildFileUri(id)
                else throw UnsupportedOperationException("Failed to insert rows :" + contentValues)
            }
            else -> throw UnsupportedOperationException("Unkown Uri: " + uri)
        }
        context.contentResolver.notifyChange(uri, null)
        return returnUri
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?,
                       selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor {
        val db = dbHelper.writableDatabase
        val cursor: Cursor
        when (uriMatcher.match(uri)) {
            REPO -> {
                cursor = db.query(
                        FileRepoContract.RepoColumns.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                )
            }
            REPO_ID -> {
                val id = ContentUris.parseId(uri)
                cursor = db.query(
                        FileRepoContract.RepoColumns.TABLE_NAME,
                        projection,
                        BaseColumns._ID + " = ?",
                        arrayOf(id.toString()),
                        null,
                        null,
                        sortOrder)
            }
            FILE -> {
                cursor = db.query(
                        FileRepoContract.FileColumns.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                )
            }
            FILE_ID -> {
                val id = ContentUris.parseId(uri)
                cursor = db.query(
                        FileRepoContract.FileColumns.TABLE_NAME,
                        projection,
                        BaseColumns._ID + " = ?",
                        arrayOf(id.toString()),
                        null,
                        null,
                        sortOrder)
            }
            else -> throw UnsupportedOperationException("Unknown Uri: " + uri)
        }
        cursor.setNotificationUri(context.contentResolver, uri)
        return cursor
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun update(uri: Uri?, contentValues: ContentValues?, selection: String?,
                        selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        val rows = when (uriMatcher.match(uri)) {
            REPO -> db.update(FileRepoContract.RepoColumns.TABLE_NAME, contentValues, selection,
                    selectionArgs)
            FILE -> db.update(FileRepoContract.FileColumns.TABLE_NAME, contentValues, selection,
                    selectionArgs)
            else -> throw UnsupportedOperationException("Unknown Uri: " + uri)
        }
        if (selection == null || rows != 0) {
            context.contentResolver.notifyChange(uri, null)
        }
        return rows
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        val rows = when (uriMatcher.match(uri)) {
            REPO -> db.delete(FileRepoContract.RepoColumns.TABLE_NAME, selection,
                    selectionArgs)
            FILE -> db.delete(FileRepoContract.FileColumns.TABLE_NAME, selection,
                    selectionArgs)
            else -> throw UnsupportedOperationException("Unknown Uri: " + uri)
        }
        if (selection == null || rows != 0) {
            context.contentResolver.notifyChange(uri, null)
        }
        return rows
    }

    override fun getType(p0: Uri?): String {
        return when (uriMatcher.match(p0)) {
            REPO -> FileRepoContract.Repo.CONTENT_TYPE
            REPO_ID -> FileRepoContract.Repo.CONTENT_ITEM_TYPE
            FILE -> FileRepoContract.File.CONTENT_TYPE
            FILE_ID -> FileRepoContract.File.CONTENT_ITEM_TYPE
            else -> throw UnsupportedOperationException("Unknown URI: " + p0)
        }
    }

    private fun buildUriMatcher(): UriMatcher {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(FileRepoContract.AUTHORITY, FileRepoContract.Repo.PATH, REPO)
        matcher.addURI(FileRepoContract.AUTHORITY, FileRepoContract.Repo.PATH + "/#", REPO_ID)
        matcher.addURI(FileRepoContract.AUTHORITY, FileRepoContract.File.PATH, FILE)
        matcher.addURI(FileRepoContract.AUTHORITY, FileRepoContract.File.PATH + "/#", FILE_ID)
        return matcher
    }

    companion object {
        val REPO = 100
        val REPO_ID = 101
        val FILE = 200
        val FILE_ID = 201
    }
}