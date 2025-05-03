package system.file

import system.GB
import system.MemorySize
import system.SingleFileResponse
import system.file.mime.Image
import system.file.mime.MediaMime
import system.file.mime.Video

interface SingleMediaPicker {
    suspend fun open(
        mimes: List<MediaMime> = listOf(Image, Video),
        limit: MemorySize = 2.GB,
    ): SingleFileResponse
}