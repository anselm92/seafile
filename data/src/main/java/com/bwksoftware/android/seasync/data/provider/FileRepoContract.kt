package com.bwksoftware.android.seasync.data.provider

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns
import android.provider.BaseColumns._ID
import com.bwksoftware.android.seasync.data.BuildConfig

class FileRepoContract {
    companion object {
        const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.sync"
        val CONTENT_URI: Uri = Uri.parse("content://" + AUTHORITY)
    }

    class Repo {
        companion object {
            val PATH = "repos"
            val CONTENT_URI: Uri = Uri.withAppendedPath(FileRepoContract.CONTENT_URI, PATH)
            val CONTENT_TYPE = """${ContentResolver.CURSOR_DIR_BASE_TYPE}/${AUTHORITY}_$PATH"""
            val CONTENT_ITEM_TYPE = """${ContentResolver.CURSOR_DIR_BASE_TYPE}/${AUTHORITY}_$PATH"""
            val PROJECTION_ALL = arrayOf(_ID, RepoColumns.NAME, RepoColumns.HASH,
                    RepoColumns.MOD_DATE)
            val SORT_ORDER_DEFAULT = "${RepoColumns.NAME} ASC"

            fun buildRepoUri(id: Long): Uri {
                return ContentUris.withAppendedId(CONTENT_URI, id)
            }
        }

    }

    interface RepoColumns : BaseColumns {
        companion object {
            val TABLE_NAME = "repo_table"
            val NAME = "repo_name"
            val HASH = "repo_hash"
            val MOD_DATE = "repo_mod_date"
            val FULL_SYNCED = "repo_full_sync"
        }
    }

    class File {
        companion object {
            val PATH = "files"
            val CONTENT_URI: Uri = Uri.withAppendedPath(FileRepoContract.CONTENT_URI, PATH)
            val CONTENT_TYPE = """${ContentResolver.CURSOR_DIR_BASE_TYPE}/${AUTHORITY}_$PATH"""
            val CONTENT_ITEM_TYPE = """${ContentResolver.CURSOR_DIR_BASE_TYPE}/${AUTHORITY}_$PATH"""
            val PROJECTION_ALL = arrayOf(_ID, FileColumns.NAME, FileColumns.HASH,
                    FileColumns.MOD_DATE, FileColumns.SIZE, FileColumns.REPO_ID, FileColumns.PARENT_ID)
            val SORT_ORDER_DEFAULT = "${FileColumns.NAME} ASC"

            fun buildFileUri(id: Long): Uri {
                return ContentUris.withAppendedId(CONTENT_URI, id)
            }
        }
    }

    interface FileColumns : BaseColumns {
        companion object {
            val TABLE_NAME = "file_table"
            val NAME = "file_name"
            val HASH = "file_hash"
            val REPO_ID = "file_repo_id"
            val PARENT_ID = "file_parent_id"
            val PATH = "file_path"
            val MOD_DATE = "file_mod_date"
            val SIZE = "file_size"
            val TYPE = "file_type"
            val REMOTE_ID = "file_remote_id"
            val SYNCED = "file_synced"
            val STORAGE = "file_storage"
        }
    }
}