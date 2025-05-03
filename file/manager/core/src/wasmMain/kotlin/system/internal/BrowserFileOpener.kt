package system.internal

import kotlinx.browser.window
import org.w3c.dom.url.URL
import system.Failure
import system.FileOpener
import system.LocalFile
import system.SingleFileResponse
import system.file.response.ResponseError

class BrowserFileOpener : FileOpener {
    override suspend fun open(file: LocalFile): SingleFileResponse {
        if (file is LocalFileUrl) return open(file.url)
        if (file is LocalFileImpl) {
            val url = URL.createObjectURL(file.wrapped)
            window.open(url, target = "_blank")
            URL.revokeObjectURL(url)
            return file
        }
        return Failure(errors = listOf(ResponseError.UnknownFileType(file)))
    }

    override suspend fun open(url: String): SingleFileResponse {
        window.open(url, "_blank")
        return LocalFileUrl(url)
    }
}