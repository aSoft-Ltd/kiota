package kiota

import kiota.file.MultiFilePicker
import kiota.file.PickerLimit
import kiota.file.mime.Mime

class JvmMultiFilePicker : AbstractFilePicker(), MultiFilePicker {
    override suspend fun open(
        mimes: List<Mime>,
        limit: PickerLimit
    ) = show(mimes, limit)
}