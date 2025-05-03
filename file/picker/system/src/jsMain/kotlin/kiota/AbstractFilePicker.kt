package kiota

import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileList
import kiota.file.PickerLimit
import kiota.file.mime.All
import kiota.file.mime.Mime
import kiota.file.toResponse
import kiota.internal.BrowserFileInfo
import kiota.internal.FileImpl
import kotlin.coroutines.resume

abstract class AbstractFilePicker {
    private fun input(mimes: List<Mime>, limit: Int) = window.document.createElement("input").apply {
        setAttribute("type", "file")
        if (mimes.contains(All)) {
            setAttribute("accept", "*/*")
        } else {
            setAttribute("accept", mimes.joinToString(",") { it.text })
        }
        if (limit > 1) setAttribute("multiple", "true")
    } as HTMLInputElement

    private suspend fun files(mimes: List<Mime>, limit: PickerLimit): List<FileImpl> {
        val input = input(mimes, limit.count)
        val files = suspendCancellableCoroutine<List<FileImpl>> { cont ->
            input.oncancel = { cont.resume(emptyList()) }
            input.onchange = { cont.resume(input.files?.toList() ?: emptyList())  }
            input.click()
        }
        input.remove()
        return files
    }

    protected suspend fun show(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse {
        val files = files(mimes, limit)
        val infos = files.map { BrowserFileInfo(it) }
        return files.toResponse(mimes, limit, infos)
    }

    private fun FileList.toList(): List<FileImpl> = buildList {
        for (i in 0 until length) {
            val file = item(i) ?: continue
            add(FileImpl(file))
        }
    }
}