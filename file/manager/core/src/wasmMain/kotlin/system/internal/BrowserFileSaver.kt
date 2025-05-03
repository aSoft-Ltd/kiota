package system.internal

import kotlinx.browser.document
import org.khronos.webgl.Int8Array
import org.khronos.webgl.set
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL
import org.w3c.files.File
import org.w3c.files.FilePropertyBag
import system.FileSaver
import system.SingleFileResponse
import system.file.mime.Mime

internal class BrowserFileSaver : FileSaver {
    override suspend fun save(content: ByteArray, name: String, type: Mime) = save(
        content = content.toJsInt8Array(),
        name = name,
        type = type
    )

    private fun ByteArray.toJsInt8Array(): Int8Array {
        val array = Int8Array(this.size)
        for (i in this.indices) {
            array[i] = this[i]
        }
        return array
    }

    override suspend fun save(content: String, name: String, type: Mime) = save(
        content = content.toJsString(),
        name = name,
        type = type
    )

    private fun save(content: JsAny?, name: String, type: Mime): SingleFileResponse {
        val file = File(arrayOf(content).toJsArray(), fileName = name, options = FilePropertyBag(type = type.text))
        val url = URL.createObjectURL(file)
        val a = document.createElement("a") as HTMLAnchorElement
        a.setAttribute("href", url)
        a.setAttribute("download", name)
        a.setAttribute("target", "_blank")
        a.click()
        a.remove()
        URL.revokeObjectURL(url)
        return LocalFileImpl(file)
    }
}