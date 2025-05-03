package system

import system.file.PickerLimit
import system.file.SingleMediaPicker
import system.file.mime.MediaMime
import system.file.response.toSingle

class BrowserSingleMediaPicker : AbstractFilePicker(), SingleMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: MemorySize) = show(mimes, PickerLimit(limit, 1)).toSingle()
}