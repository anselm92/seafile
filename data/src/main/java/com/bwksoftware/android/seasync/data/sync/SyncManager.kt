package com.bwksoftware.android.seasync.data.sync

import android.os.AsyncTask
import android.util.Log
import com.bwksoftware.android.seasync.data.entity.Item
import okhttp3.ResponseBody
import java.io.*

/**
 * Created by anselm.binninger on 24/10/2017.
 */
class SyncManager {

    class DownloadTask(val localItem: Item,
                       val responseBody: ResponseBody) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg p0: Void?): Boolean {
            writeResponseBodyToDisk(
                    localItem.path + "/" + localItem.name, responseBody)
            return true
        }

        private fun writeResponseBodyToDisk(path: String, body: ResponseBody): Boolean {
            try {
                // todo change the file location/name according to your needs
                val futureStudioIconFile = File(path)

                var inputStream: InputStream? = null
                var outputStream: OutputStream? = null

                try {
                    val fileReader = ByteArray(4096)

                    val fileSize = body.contentLength()
                    var fileSizeDownloaded: Long = 0

                    inputStream = body.byteStream()
                    outputStream = FileOutputStream(futureStudioIconFile)

                    while (true) {
                        val read = inputStream!!.read(fileReader)

                        if (read == -1) {
                            break
                        }

                        outputStream!!.write(fileReader, 0, read)

                        fileSizeDownloaded += read.toLong()

                        Log.d("bla", "file download: $fileSizeDownloaded of $fileSize")
                    }

                    outputStream!!.flush()

                    return true
                } catch (e: IOException) {
                    return false
                } finally {
                    if (inputStream != null) {
                        inputStream!!.close()
                    }

                    if (outputStream != null) {
                        outputStream!!.close()
                    }
                }
            } catch (e: IOException) {
                return false
            }

        }

    }

}