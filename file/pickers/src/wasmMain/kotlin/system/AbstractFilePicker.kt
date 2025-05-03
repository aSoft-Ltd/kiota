package system

import kotlinx.browser.window
import kotlinx.coroutines.suspendCancellableCoroutine
import org.w3c.dom.HTMLInputElement
import org.w3c.files.FileList
import system.file.PickerLimit
import system.file.mime.All
import system.file.mime.Mime
import system.file.toResponse
import system.internal.BrowserFileInfo
import system.internal.LocalFileImpl
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

    private suspend fun files(mimes: List<Mime>, limit: PickerLimit): List<LocalFileImpl> {
        val input = input(mimes, limit.count)
        val files = suspendCancellableCoroutine<List<LocalFileImpl>> { cont ->
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

    private fun FileList.toList(): List<LocalFileImpl> = buildList {
        for (i in 0 until length) {
            val file = item(i) ?: continue
            add(LocalFileImpl(file))
        }
    }
}