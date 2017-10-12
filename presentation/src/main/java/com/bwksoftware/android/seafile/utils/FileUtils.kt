package com.bwksoftware.android.seafile.utils

import android.webkit.MimeTypeMap
import java.net.URLEncoder


class FileUtils {
    companion object {
        fun isViewableImage(fileName: String): Boolean {
            val encodedFileName = URLEncoder.encode(fileName,"UTF-8").replace("+","%20").toLowerCase()
            val extension = MimeTypeMap.getFileExtensionFromUrl(encodedFileName)
            val mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
            if (mime != null)
                return mime.contains("image")
            else return false
        }

        fun getThumbnailUrl(address: String, repoId: String, fileName: String, size: Int): String {
            return "https://$address/api2/repos/$repoId/thumbnail/?p=$fileName&size=$size"
        }
    }
}