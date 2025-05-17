package kiota.internal

import kiota.File
import kiota.FileCreator
import kiota.file.mime.Mime
import org.khronos.webgl.Int8Array
import org.khronos.webgl.set
import org.w3c.files.FilePropertyBag
import org.w3c.files.File as BFile

internal class BrowserFileCreator : FileCreator {
    override suspend fun create(content: ByteArray, name: String, type: Mime) = save(
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

    override suspend fun create(content: String, name: String, type: Mime) = save(
        content = content,
        name = name,
        type = type
    )

    private fun save(content: Any?, name: String, type: Mime): File {
        val file = BFile(arrayOf(content), fileName = name, options = FilePropertyBag(type = type.text))
        return FileImpl(file)
    }
}