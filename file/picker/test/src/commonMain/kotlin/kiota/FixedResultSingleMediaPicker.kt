package kiota

import kiota.file.SingleMediaPicker
import kiota.file.mime.MediaMime

class FixedResultSingleMediaPicker(private val result: Denied) : SingleMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: MemorySize): SingleFileResponse = result
}