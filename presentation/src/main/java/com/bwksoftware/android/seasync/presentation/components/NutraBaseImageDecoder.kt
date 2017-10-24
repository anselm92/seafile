package com.bwksoftware.android.seasync.presentation.components

import com.nostra13.universalimageloader.core.decode.BaseImageDecoder
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo
import java.io.IOException
import java.io.InputStream


/**
 * Created by anselm.binninger on 13/10/2017.
 */
class NutraBaseImageDecoder(loggingEnabled: Boolean) : BaseImageDecoder(loggingEnabled) {

    @Throws(IOException::class)
    override fun getImageStream(decodingInfo: ImageDecodingInfo): InputStream? {
        val stream = decodingInfo.downloader
                .getStream(decodingInfo.imageUri, decodingInfo.extraForDownloader)

        return if (stream == null) null else JpegClosedInputStream(stream)
    }

    private inner class JpegClosedInputStream(
            private val inputStream: InputStream) : InputStream() {
        private var bytesPastEnd: Int = 0

        init {
            bytesPastEnd = 0
        }

        @Throws(IOException::class)
        override fun read(): Int {
            var buffer = inputStream.read()
            if (buffer == -1) {
                if (bytesPastEnd > 0) {
                    buffer = JPEG_EOI_2
                } else {
                    ++bytesPastEnd
                    buffer = JPEG_EOI_1
                }
            }

            return buffer
        }


    }

    companion object {

        private val JPEG_EOI_1 = 0xFF
        private val JPEG_EOI_2 = 0xD9
    }
}