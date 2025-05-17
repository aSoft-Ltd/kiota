package kiota.internal

import kiota.Cancelled
import kiota.Failure
import kiota.File
import kiota.FileExposer
import kiota.SingleFileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import kotlin.coroutines.resume
import java.io.File as JFile

internal class JvmFileExposer : FileExposer {

    private suspend fun directory(): JFile? {
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

    override suspend fun export(file: File): SingleFileResponse {
        val src = file.toFileOrNull() ?: return Failure(errors = listOf())
        val directory = directory() ?: return Cancelled
        return withContext(Dispatchers.IO) {
            val dst = JFile(directory, src.name).apply { createNewFile() }
            val sis = FileInputStream(src)
            val dos = FileOutputStream(dst)
            dos.write(sis.readAllBytes())
            sis.close()
            dos.flush()
            dos.close()
            FileImpl(dst.path)
        }
    }

    override suspend fun share(file: File): SingleFileResponse = export(file)
}