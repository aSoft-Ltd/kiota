package system

import system.file.MultiFilePicker
import system.file.PickerLimit
import system.file.mime.Mime

class OSXMultiFilePicker : OSXFilePicker(), MultiFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: PickerLimit) = show(mimes, limit)
}