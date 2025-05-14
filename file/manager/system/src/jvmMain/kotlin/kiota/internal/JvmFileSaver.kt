package kiota.internal

import kiota.Cancelled
import kiota.Failure
import kiota.FileSaver
import kiota.SingleFileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
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

    override suspend fun saveAs(file: kiota.File): SingleFileResponse {
        val src = file.toFileOrNull() ?: return Failure(errors = listOf())
        val directory = directory() ?: return Cancelled
        return withContext(Dispatchers.IO) {
            val dst = File(directory, src.name).apply { createNewFile() }
            val sis = FileInputStream(src)
            val dos = FileOutputStream(dst)
            dos.write(sis.readAllBytes())
            sis.close()
            dos.flush()
            dos.close()
            FileImpl(dst.path)
        }
    }
}