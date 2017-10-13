package com.bwksoftware.android.seafile.utils

import android.content.Context
import android.webkit.MimeTypeMap
import com.bwksoftware.android.seafile.R
import java.net.URLEncoder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


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

        fun readableFileSize(size: Long): String {
            if (size <= 0) return "0 KB"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1000.0)).toInt()
            return DecimalFormat("#,##0.#").format(
                    size / Math.pow(1000.0, digitGroups.toDouble())) + " " + units[digitGroups]
        }

        fun translateCommitTime(timestampInMillis: Long, context: Context): String {
            val now = Calendar.getInstance().timeInMillis
            if (now <= timestampInMillis) {
                return context.getString(R.string.just_now)
            }

            val delta = (now - timestampInMillis) / 1000

            val secondsPerDay = (24 * 60 * 60).toLong()

            val days = delta / secondsPerDay
            val seconds = delta % secondsPerDay

            if (days >= 14) {
                val d = Date(timestampInMillis)
                val fmt = SimpleDateFormat("yyyy-MM-dd")
                return fmt.format(d)
            } else if (days > 0) {
                return context.getString(R.string.days_ago, days)
            } else if (seconds >= 60 * 60) {
                val hours = seconds / 3600
                return context.getString(R.string.hours_ago, hours)
            } else if (seconds >= 60) {
                val minutes = seconds / 60
                return context.getString(R.string.minutes_ago, minutes)
            } else if (seconds > 0) {
                return context.getString(R.string.seconds_ago, seconds)
            } else {
                return context.getString(R.string.just_now)
            }
        }

    }

}