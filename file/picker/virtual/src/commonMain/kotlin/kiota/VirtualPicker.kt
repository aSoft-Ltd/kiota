package kiota

import kiota.file.PickerLimit
import kiota.file.mime.Mime

abstract class VirtualPicker(private val files: MutableMap<String, File>) {
    fun show(mimes: Iterable<Mime>, limit: PickerLimit): MultiPickingResult<PickingExplanation> {
        val res = files.values.take(limit.count)
        if (res.isEmpty()) return Cancelled
        return Files(res)
    }
}