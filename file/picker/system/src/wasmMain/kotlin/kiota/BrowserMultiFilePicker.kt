package kiota

import kiota.file.MultiFilePicker
import kiota.file.PickerLimit
import kiota.file.mime.Mime

class BrowserMultiFilePicker : AbstractFilePicker(), MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse = show(mimes, limit)
}