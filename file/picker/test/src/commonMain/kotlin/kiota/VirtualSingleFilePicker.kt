package kiota

import kiota.file.PickerLimit
import kiota.file.SingleFilePicker
import kiota.file.mime.Mime
import kiota.file.response.toSingle

class VirtualSingleFilePicker(private val files: MutableMap<String, File>) : VirtualFilePicker(files), SingleFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: MemorySize) = show(mimes, PickerLimit(limit, 1)).toSingle()
}