package kiota

import kiota.file.MultiMediaPicker
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime

class BrowserMultiMediaPicker : AbstractFilePicker(), MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit): MultiPickerResponse = show(mimes, limit)
}