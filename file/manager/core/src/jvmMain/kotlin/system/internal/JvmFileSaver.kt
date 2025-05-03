package system.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import system.Cancelled
import system.FileSaver
import system.SingleFileResponse
import system.file.mime.Mime
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

internal class JvmFileSaver : FileSaver {

    private suspend fun directory(): File? {
        val chooser = JFileChooser().apply {
            fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
            isMultiSelectionEnabled = false
            this.name = "Select Directory"
        }

        return suspendCancellableCoroutine { cont ->
            SwingUtilities.invokeLater {
                val result = chooser.showOpenDialog(null)
                val response = when (result) {
                    JFileChooser.APPROVE_OPTION -> chooser.selectedFile
                    else -> null
                }
                cont.resume(response)
            }
        }
    }

    private suspend fun file(name: String, block: File.() -> Unit): SingleFileResponse {
        val directory = directory() ?: return Cancelled
        return withContext(Dispatchers.IO) {
            val file = File(directory, name).apply { createNewFile() }
            block(file)
            LocalFileImpl(file.path)
        }
    }

    override suspend fun save(content: ByteArray, name: String, type: Mime) = file(name) { writeBytes(content) }

    override suspend fun save(content: String, name: String, type: Mime) = file(name) { writeText(content) }
}