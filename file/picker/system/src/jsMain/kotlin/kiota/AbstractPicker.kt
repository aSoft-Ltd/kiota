package kiota

import kiota.file.PickerLimit
import kiota.file.mime.All
import kiota.file.mime.Mime
import kiota.file.toResult
import kiota.internal.BrowserFileInfo
import kiota.internal.FileImpl
import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileList
import kotlin.coroutines.resume

abstract class AbstractPicker {
    private fun input(mimes: Collection<Mime>, limit: Int) = window.document.createElement("input").apply {
        setAttribute("type", "file")
        if (mimes.contains(All)) {
            setAttribute("accept", "*/*")
        } else {
            setAttribute("accept", mimes.joinToString(",") { it.text })
        }
        if (limit > 1) setAttribute("multiple", "true")
    } as HTMLInputElement

    private suspend fun files(mimes: Collection<Mime>, limit: PickerLimit): List<FileImpl> {
        val input = input(mimes, limit.count)
        val files = suspendCancellableCoroutine<List<FileImpl>> { cont ->
            input.oncancel = { cont.resume(emptyList()) }
            input.onchange = { cont.resume(input.files?.toList() ?: emptyList()) }
            input.click()
        }
        input.remove()
        return files
    }

    protected suspend fun show(mimes: Collection<Mime>, limit: PickerLimit): MultiPickingResult {
        val files = files(mimes, limit)
        val infos = files.map { BrowserFileInfo(it) }
        return files.toResult(mimes, limit, infos)
    }

    private fun FileList.toList(): List<FileImpl> = buildList {
        for (i in 0 until length) {
            val file = item(i) ?: continue
            add(FileImpl(file, FileScope.public))
        }
    }
}