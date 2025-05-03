package system

import system.file.PickerLimit
import system.file.SingleMediaPicker
import system.file.mime.MediaMime
import system.file.response.toSingle

class OSXSingleMediaPicker : OSXMediaPicker(), SingleMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: MemorySize) = show(mimes, PickerLimit(size = limit, count = 1)).toSingle()
}