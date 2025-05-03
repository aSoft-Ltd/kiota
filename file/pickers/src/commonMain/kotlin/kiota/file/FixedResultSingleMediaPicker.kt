package kiota.file

import kiota.Denied
import kiota.MemorySize
import kiota.SingleFileResponse
import kiota.file.mime.MediaMime

class FixedResultSingleMediaPicker(private val result: Denied) : SingleMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: MemorySize): SingleFileResponse = result
}