package kiota.internal

import kiota.Failure
import kiota.File
import kiota.FileExportExplanation
import kiota.FileExportResult
import kiota.FileExposer
import kiota.UnknownFile
import kotlinx.browser.document
import org.w3c.dom.HTMLAnchorElement

internal class BrowserFileExposer : FileExposer {
    override suspend fun export(file: File): FileExportResult<FileExportExplanation> {
        return when (file) {
            is FileUrl -> save(file, file.url, file.name)
            is FileImpl -> file.withUrl { save(file, it, file.wrapped.name) }
            else -> Failure(UnknownFile(file))
        }
    }

    private fun save(file: File, url: String, name: String): File {
        val a = document.createElement("a") as HTMLAnchorElement
        a.setAttribute("href", url)
        a.setAttribute("download", name)
        a.setAttribute("target", "_blank")
        a.click()
        a.remove()
        return file
    }

    override suspend fun share(file: File) = export(file)
}