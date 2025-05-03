package system.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import system.Failure
import system.FileOpener
import system.LocalFile
import system.SingleFileResponse
import system.file.response.ResponseError
import java.awt.Desktop
import java.io.File

class JvmFileOpener : FileOpener {

    override suspend fun open(file: LocalFile): SingleFileResponse = when (file) {
        is LocalFileImpl -> open(file.path)
        else -> Failure(errors = listOf(ResponseError.UnknownFileType(file)))
    }

    override suspend fun open(url: String): SingleFileResponse = withContext(Dispatchers.IO) {
        Desktop.getDesktop().open(File(url))
        LocalFileImpl(url)
    }
}