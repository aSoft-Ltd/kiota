package system

import system.file.MultiMediaPicker
import system.file.PickerLimit
import system.file.mime.MediaMime

class OSXMultiMediaPicker : OSXMediaPicker(), MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit) = show(mimes, limit)
}