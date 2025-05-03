package kiota

import kiota.file.MultiFilePicker
import kiota.file.PickerLimit
import kiota.file.mime.Mime

class VirtualMultiFilePicker(private val files: MutableMap<String, File>) : VirtualFilePicker(files), MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit) = show(mimes, limit)
}