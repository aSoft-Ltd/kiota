package system

import system.file.PickerLimit
import system.file.SingleFilePicker
import system.file.mime.Mime
import system.file.response.toSingle

class JvmSingleFilePicker : AbstractFilePicker(), SingleFilePicker {
    override suspend fun open(
        mimes: List<Mime>,
        limit: MemorySize
    ) = show(mimes, PickerLimit(limit, 1)).toSingle()
}