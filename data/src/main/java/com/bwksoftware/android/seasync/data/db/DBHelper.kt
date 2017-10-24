package com.bwksoftware.android.seasync.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.bwksoftware.android.seasync.data.provider.FileRepoContract
import javax.inject.Inject


class DBHelper @Inject constructor(val context: Context) : SQLiteOpenHelper(context, DATABASE_NAME,
        null,
        DATABASE_VERSION) {

    override fun onCreate(p0: SQLiteDatabase?) {
        createFileTable(p0!!)
        createRepoTable(p0)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun createRepoTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE " + FileRepoContract.RepoColumns.TABLE_NAME + " (" +
                        BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                        FileRepoContract.RepoColumns.NAME + " TEXT, " +
                        FileRepoContract.RepoColumns.HASH + " TEXT, " +
                        FileRepoContract.RepoColumns.FULL_SYNCED + " INTEGER, " +
                        FileRepoContract.RepoColumns.MOD_DATE + " INTEGER);"
        )
    }

    fun createFileTable(db: SQLiteDatabase) {
        db.execSQL(
                "CREATE TABLE " + FileRepoContract.FileColumns.TABLE_NAME + " (" +
                        BaseColumns._ID + " INTEGER PRIMARY KEY, " +
                        FileRepoContract.FileColumns.NAME + " TEXT, " +
                        FileRepoContract.FileColumns.HASH + " TEXT, " +
                        FileRepoContract.FileColumns.PARENT_ID + " INTEGER, " +
                        FileRepoContract.FileColumns.REPO_ID + " INTEGER, " +
                        FileRepoContract.FileColumns.PATH + " TEXT, " +
                        FileRepoContract.FileColumns.TYPE + " TEXT, " +
                        FileRepoContract.FileColumns.SIZE + " INTEGER, " +
                        FileRepoContract.FileColumns.SYNCED + " INTEGER, " +
                        FileRepoContract.FileColumns.MOD_DATE + " INTEGER);"
        )
    }

    companion object {
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "seasync.db"
    }
}