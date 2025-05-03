package kiota.file

import kiota.GB
import kiota.MemorySize
import kiota.SingleFileResponse
import kiota.file.mime.All
import kiota.file.mime.Mime

interface SingleFilePicker {
    suspend fun open(
        mimes: List<Mime> = listOf(All),
        limit: MemorySize = 2.GB,
    ): SingleFileResponse
}