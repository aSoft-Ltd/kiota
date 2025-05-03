package system

import system.file.MultiFilePicker
import system.file.PickerLimit
import system.file.mime.Mime

class BrowserMultiFilePicker : AbstractFilePicker(), MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse = show(mimes, limit)
}