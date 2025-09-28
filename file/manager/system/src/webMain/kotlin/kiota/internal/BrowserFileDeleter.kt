package kiota.internal

import kiota.File
import kiota.FileDeleter
import org.w3c.dom.url.URL

internal class BrowserFileDeleter : FileDeleter {
    override fun delete(file: File): Boolean = when (file) {
        is FileUrl -> {
            URL.revokeObjectURL(file.url)
            true
        }

        is FileImpl -> {
            true
        }

        else -> false
    }
}