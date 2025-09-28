package kiota.internal

import kiota.Failure
import kiota.File
import kiota.FileOpenExplanation
import kiota.FileOpenResult
import kiota.FileOpener
import kiota.UnknownFile
import kotlinx.browser.window

class BrowserFileOpener : FileOpener {
    override suspend fun open(file: File): FileOpenResult<FileOpenExplanation> {
        if (file is FileUrl) return open(file.url)
        if (file is FileImpl) return file.withUrl { window.open(it, "_blank") }
        return Failure(UnknownFile(file))
    }

    override suspend fun open(url: String): FileOpenResult<FileOpenExplanation> {
        window.open(url, "_blank")
        return FileUrl(url, "untitled")
    }
}