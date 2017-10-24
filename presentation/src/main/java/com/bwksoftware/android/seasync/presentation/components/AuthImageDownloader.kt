package com.bwksoftware.android.seasync.presentation.components

import android.content.Context
import com.github.kevinsawicki.http.HttpRequest
import com.nostra13.universalimageloader.core.assist.FlushedInputStream
import com.nostra13.universalimageloader.core.download.BaseImageDownloader
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream


class AuthImageDownloader(context: Context, connectTimeout: Int,
                          readTimeout: Int) : BaseImageDownloader(context, connectTimeout,
        readTimeout) {

    @Throws(IOException::class)
    override fun getStreamFromNetwork(imageUri: String, extra: Any): InputStream {
        val req = HttpRequest.get(imageUri, null, false)
                .readTimeout(readTimeout)
                .connectTimeout(connectTimeout)
                .followRedirects(true)
                .header("Authorization", "Token " + (extra as String))

        val conn = req.getConnection()

        req.trustAllHosts()



        return FlushedInputStream(BufferedInputStream(
                req.stream()))
    }

    companion object {
        val TAG = AuthImageDownloader::class.java.name
    }
}