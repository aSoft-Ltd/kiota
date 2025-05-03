package system.file

import system.MultiPickerResponse
import system.file.mime.Image
import system.file.mime.MediaMime
import system.file.mime.Video

interface MultiMediaPicker {
    suspend fun open(
        mimes: List<MediaMime> = listOf(Image, Video),
        limit: PickerLimit = PickerLimit.Default,
    ): MultiPickerResponse
}