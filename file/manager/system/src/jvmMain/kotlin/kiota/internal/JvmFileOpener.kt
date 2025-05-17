package kiota.internal

import kiota.Failure
import kiota.FileOpenExplanation
import kiota.FileOpenResult
import kiota.FileOpener
import kiota.UnknownFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.Desktop
import java.io.File as JvmFile

class JvmFileOpener : FileOpener {

    override suspend fun open(file: kiota.File): FileOpenResult<FileOpenExplanation> = when (file) {
        is FileImpl -> open(file.path)
        else -> Failure(UnknownFile(file))
    }

    override suspend fun open(url: String): FileOpenResult<FileOpenExplanation> = withContext(Dispatchers.IO) {
        Desktop.getDesktop().open(JvmFile(url))
        FileImpl(url)
    }
}