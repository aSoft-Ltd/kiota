package kiota.file

import kiota.Denied
import kiota.MultiPickerResponse
import kiota.file.mime.Mime

class FixedResultMultiFilePicker(private val result: Denied) : MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse = result
}