package kiota.internal

import kotlinx.browser.window
import org.w3c.dom.url.URL
import kiota.Failure
import kiota.FileOpener
import kiota.File
import kiota.SingleFileResponse
import kiota.file.response.ResponseError

class BrowserFileOpener : FileOpener {
    override suspend fun open(file: File): SingleFileResponse {
        if (file is FileUrl) return open(file.url)
        if (file is FileImpl) {
            val url = URL.createObjectURL(file.wrapped)
            window.open(url, target = "_blank")
            URL.revokeObjectURL(url)
            return file
        }
        return Failure(errors = listOf(ResponseError.UnknownFileType(file)))
    }

    override suspend fun open(url: String): SingleFileResponse {
        window.open(url, "_blank")
        return FileUrl(url)
    }
}