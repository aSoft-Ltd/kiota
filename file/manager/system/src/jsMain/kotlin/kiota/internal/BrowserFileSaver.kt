package kiota.internal

import kiota.Failure
import kiota.FileSaver
import kiota.SingleFileResponse
import kiota.file.response.ResponseError
import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL

internal class BrowserFileSaver : FileSaver {
    override suspend fun saveAs(file: kiota.File): SingleFileResponse {
        when (file) {
            is FileUrl -> {
                save(file.url, file.name)
                return file
            }

            is FileImpl -> {
                val url = URL.createObjectURL(file.wrapped)
                save(url, file.wrapped.name)
                val res = FileUrl(url, file.wrapped.name)
                URL.revokeObjectURL(url)
                return res
            }

            else -> return Failure(errors = listOf(ResponseError.UnknownFileType(file)))
        }
    }

    private fun save(url: String, name: String) {
        val a = document.createElement("a") as HTMLAnchorElement
        a.setAttribute("href", url)
        a.setAttribute("download", name)
        a.setAttribute("target", "_blank")
        a.click()
        a.remove()
    }
}