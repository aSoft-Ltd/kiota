package kiota.internal

import kiota.Failure
import kiota.File
import kiota.FileExposer
import kiota.SingleFileResponse
import kiota.file.response.ResponseError
import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL

internal class BrowserFileExposer : FileExposer {
    override suspend fun export(file: File): SingleFileResponse = when (file) {
        is FileUrl -> {
            save(file.url, file.name)
            file
        }

        is FileImpl -> {
            val url = URL.createObjectURL(file.wrapped)
            save(url, file.wrapped.name)
            val res = FileUrl(url, file.wrapped.name)
            URL.revokeObjectURL(url)
            res
        }

        else -> Failure(reasons = listOf(ResponseError.UnknownFileType(file)))
    }

    private fun save(url: String, name: String) {
        val a = document.createElement("a") as HTMLAnchorElement
        a.setAttribute("href", url)
        a.setAttribute("download", name)
        a.setAttribute("target", "_blank")
        a.click()
        a.remove()
    }

    override suspend fun share(file: File): SingleFileResponse = export(file)
}