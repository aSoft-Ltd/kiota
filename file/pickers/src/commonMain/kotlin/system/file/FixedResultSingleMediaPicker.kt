package system.file

import system.Denied
import system.MemorySize
import system.SingleFileResponse
import system.file.mime.MediaMime

class FixedResultSingleMediaPicker(private val result: Denied) : SingleMediaPicker {
    override suspend fun open(mimes: List<MediaMime>, limit: MemorySize): SingleFileResponse = result
}