package system.file

import system.Denied
import system.MultiPickerResponse
import system.file.mime.MediaMime

class FixedResultMultiMediaPicker(private val result: Denied) : MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit): MultiPickerResponse = result
}