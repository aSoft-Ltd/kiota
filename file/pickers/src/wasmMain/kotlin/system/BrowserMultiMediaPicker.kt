package system

import system.file.MultiMediaPicker
import system.file.PickerLimit
import system.file.mime.MediaMime

class BrowserMultiMediaPicker : AbstractFilePicker(), MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit): MultiPickerResponse = show(mimes, limit)
}