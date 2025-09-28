package kiota.internal

import kiota.File
import org.w3c.dom.url.URL
import org.w3c.files.File as BrowserFile

data class FileImpl(val wrapped: BrowserFile) : File {

    inline fun withUrl(block: (String) -> Unit): FileImpl {
        val url = URL.createObjectURL(wrapped)
        block(url)
        URL.revokeObjectURL(url)
        return this
    }
}