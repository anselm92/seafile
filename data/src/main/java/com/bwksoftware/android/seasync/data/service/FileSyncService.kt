package com.bwksoftware.android.seasync.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log


class FileSyncService : Service() {

    private var syncAdapter: FileSyncAdapter? = null

    private val sSyncAdapterLock = Any()


    override fun onCreate() {
        synchronized(sSyncAdapterLock) {
            if (syncAdapter == null)
                syncAdapter = FileSyncAdapter(applicationContext)
        }
    }

    override fun onBind(p0: Intent?): IBinder {
        Log.d("FileSyncService", "Syncservice binded")
        return syncAdapter!!.syncAdapterBinder
    }

}