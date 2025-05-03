package kiota.file

import kiota.MultiPickerResponse
import kiota.file.mime.Image
import kiota.file.mime.MediaMime
import kiota.file.mime.Video

interface MultiMediaPicker {
    suspend fun open(
        mimes: List<MediaMime> = listOf(Image, Video),
        limit: PickerLimit = PickerLimit.Default,
    ): MultiPickerResponse
}