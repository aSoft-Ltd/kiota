package kiota

import kiota.file.PickerLimit
import kiota.file.SingleMediaPicker
import kiota.file.internal.toMediaMimes
import kiota.file.mime.MediaMime
import kiota.file.response.toSingle

class JvmSingleMediaPicker : AbstractFilePicker(), SingleMediaPicker {
    override suspend fun open(
        mimes: List<MediaMime>,
        limit: MemorySize
    ) = show(mimes.toMediaMimes(), PickerLimit(limit, 1)).toSingle()
}