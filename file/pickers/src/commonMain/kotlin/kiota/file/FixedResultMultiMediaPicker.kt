package kiota.file

import kiota.Denied
import kiota.MultiPickerResponse
import kiota.file.mime.MediaMime

class FixedResultMultiMediaPicker(private val result: Denied) : MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit): MultiPickerResponse = result
}