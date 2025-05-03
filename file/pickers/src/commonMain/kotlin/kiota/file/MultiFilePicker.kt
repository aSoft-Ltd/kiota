package kiota.file

import kiota.MultiPickerResponse
import kiota.file.mime.All
import kiota.file.mime.Mime

interface MultiFilePicker {
    suspend fun open(
        mimes: List<Mime> = listOf(All),
        limit: PickerLimit = PickerLimit.Default,
    ): MultiPickerResponse
}