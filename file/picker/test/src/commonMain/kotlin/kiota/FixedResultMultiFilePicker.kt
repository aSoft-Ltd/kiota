package kiota

import kiota.file.MultiFilePicker
import kiota.file.PickerLimit
import kiota.file.mime.Mime

class FixedResultMultiFilePicker(private val result: Denied) : MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse = result
}