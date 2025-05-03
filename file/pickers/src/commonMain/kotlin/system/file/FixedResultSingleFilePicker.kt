package system.file

import system.Denied
import system.MemorySize
import system.SingleFileResponse
import system.file.mime.Mime

class FixedResultSingleFilePicker(private val result: Denied) : SingleFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: MemorySize): SingleFileResponse = result
}