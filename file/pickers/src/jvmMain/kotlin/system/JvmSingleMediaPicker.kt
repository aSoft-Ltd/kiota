package system

import system.file.PickerLimit
import system.file.SingleMediaPicker
import system.file.internal.toMediaMimes
import system.file.mime.MediaMime
import system.file.response.toSingle

class JvmSingleMediaPicker : AbstractFilePicker(), SingleMediaPicker {
    override suspend fun open(
        mimes: List<MediaMime>,
        limit: MemorySize
    ) = show(mimes.toMediaMimes(), PickerLimit(limit, 1)).toSingle()
}