package kiota

import kiota.file.PickerLimit
import kiota.file.SingleMediaPicker
import kiota.file.mime.MediaMime
import kiota.file.response.toSingle

class OSXSingleMediaPicker : OSXMediaPicker(), SingleMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: MemorySize) = show(mimes, PickerLimit(size = limit, count = 1)).toSingle()
}