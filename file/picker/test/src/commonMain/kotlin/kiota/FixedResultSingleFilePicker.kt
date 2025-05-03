package kiota

import kiota.file.SingleFilePicker
import kiota.file.mime.Mime

class FixedResultSingleFilePicker(private val result: Denied) : SingleFilePicker {
    override suspend fun open(mimes: List<Mime>, limit: MemorySize): SingleFileResponse = result
}