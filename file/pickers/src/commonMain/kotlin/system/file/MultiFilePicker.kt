package system.file

import system.MultiPickerResponse
import system.file.mime.All
import system.file.mime.Mime

interface MultiFilePicker {
    suspend fun open(
        mimes: List<Mime> = listOf(All),
        limit: PickerLimit = PickerLimit.Default,
    ): MultiPickerResponse
}