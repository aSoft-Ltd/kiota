package system

import kotlinx.coroutines.suspendCancellableCoroutine
import system.file.PickerLimit
import system.file.mime.All
import system.file.mime.Mime
import system.file.toResponse
import system.internal.FileInfoImpl
import system.internal.LocalFileImpl
import system.internal.MimeFileFilter
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

abstract class AbstractFilePicker {
    protected suspend fun show(
        mimes: List<Mime>,
        limit: PickerLimit
    ): MultiPickerResponse {
        if (mimes.isEmpty()) return show(listOf(All), limit)
        if (limit.count <= 0) return Cancelled
        if (limit.size <= MemorySize.Zero) return Cancelled

        val chooser = JFileChooser().apply {
            fileSelectionMode = JFileChooser.FILES_ONLY
            isMultiSelectionEnabled = limit.count > 1
            fileFilter = MimeFileFilter(mimes, limit.size)
            name = "Select File" + if (limit.count > 1) "s" else ""
        }

        val files = suspendCancellableCoroutine { cont ->
            SwingUtilities.invokeLater {
                val result = chooser.showOpenDialog(null)
                val response = when (result) {
                    JFileChooser.APPROVE_OPTION -> chooser.selectedFiles + chooser.selectedFile
                    else -> emptyArray()
                }
                cont.resume(response)
            }
        }.mapNotNull { it.path }.toSet().map { LocalFileImpl(it) }
        val infos = files.map { FileInfoImpl(it) }
        return files.toResponse(mimes, limit, infos)
    }
}