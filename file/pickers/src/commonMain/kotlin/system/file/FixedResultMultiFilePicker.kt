package system.file

import system.Denied
import system.MultiPickerResponse
import system.file.mime.Mime

class FixedResultMultiFilePicker(private val result: Denied) : MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse = result
}