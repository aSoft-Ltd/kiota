package kiota

import kiota.file.PickerLimit
import kiota.file.mime.Mime

abstract class VirtualFilePicker(private val files: MutableMap<String, File>) {
    fun show(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse {
        val res = files.values.take(limit.count)
        if (res.isEmpty()) return Cancelled
        return Files(res)
    }
}