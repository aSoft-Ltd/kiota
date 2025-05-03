package kiota

import kiota.file.MultiMediaPicker
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime

class OSXMultiMediaPicker : OSXMediaPicker(), MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit) = show(mimes, limit)
}