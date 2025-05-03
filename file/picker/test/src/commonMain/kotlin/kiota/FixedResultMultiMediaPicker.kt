package kiota

import kiota.file.MultiMediaPicker
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime

class FixedResultMultiMediaPicker(private val result: Denied) : MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit): MultiPickerResponse = result
}