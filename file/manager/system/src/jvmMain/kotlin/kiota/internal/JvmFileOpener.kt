package kiota.internal

import kiota.Failure
import kiota.FileOpener
import kiota.SingleFileResponse
import kiota.file.response.ResponseError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.io.File as JvmFile

class JvmFileOpener : FileOpener {

    override suspend fun open(file: kiota.File): SingleFileResponse = when (file) {
        is FileImpl -> open(file.path)
        else -> Failure(errors = listOf(ResponseError.UnknownFileType(file)))
    }

    override suspend fun open(url: String): SingleFileResponse = withContext(Dispatchers.IO) {
        Desktop.getDesktop().open(JvmFile(url))
        FileImpl(url)
    }
}