package kiota.file

import kiota.GB
import kiota.MemorySize
import kiota.SingleFileResponse
import kiota.file.mime.Image
import kiota.file.mime.MediaMime
import kiota.file.mime.Video

interface SingleMediaPicker {
    suspend fun open(
        mimes: List<MediaMime> = listOf(Image, Video),
        limit: MemorySize = 2.GB,
    ): SingleFileResponse
}