package kiota

import kiota.file.PickerLimit
import kiota.file.mime.All
import kiota.file.mime.Mime
import kiota.file.toResult
import kiota.internal.FileImpl
import kiota.internal.FileInfoImpl
import kiota.internal.MimeFileFilter
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

abstract class AbstractPicker {

    private val chooser by lazy { JFileChooser() }

    protected suspend fun show(
        mimes: Collection<Mime>,
        limit: PickerLimit
    ): MultiPickingResult<PickingExplanation> {
        if (mimes.isEmpty()) return show(listOf(All), limit)
        if (limit.count <= 0) return Cancelled
        if (limit.size <= MemorySize.Zero) return Cancelled

        chooser.apply {
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
        }.mapNotNull { it.path }.toSet().map { FileImpl(it) }
        val infos = files.map { FileInfoImpl(it) }
        return files.toResult(mimes, limit, infos)
    }
}