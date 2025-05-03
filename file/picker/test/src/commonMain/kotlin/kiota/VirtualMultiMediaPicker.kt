package kiota

import kiota.file.MultiMediaPicker
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime

class VirtualMultiMediaPicker(private val files: MutableMap<String, File>) : VirtualFilePicker(files), MultiMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: PickerLimit) = show(mimes, limit)
}