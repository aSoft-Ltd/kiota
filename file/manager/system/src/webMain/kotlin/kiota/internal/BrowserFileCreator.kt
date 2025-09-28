package kiota.internal

import kiota.FileCreationExplanation
import kiota.FileCreationResult
import kiota.FileCreator
import kiota.file.mime.Mime
import org.khronos.webgl.Int8Array
import org.khronos.webgl.set
import org.w3c.files.File
import org.w3c.files.FilePropertyBag
import kotlin.js.JsAny
import kotlin.js.toJsArray
import kotlin.js.toJsString

internal class BrowserFileCreator : FileCreator {
    override suspend fun create(content: ByteArray, name: String, type: Mime) = create(
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

    override suspend fun create(content: String, name: String, type: Mime) : FileCreationResult<FileCreationExplanation> = create(
        content = content.toJsString(),
        name = name,
        type = type
    )

    private fun create(content: JsAny?, name: String, type: Mime): FileCreationResult<FileCreationExplanation> {
        val file = File(arrayOf(content).toJsArray(), fileName = name, options = FilePropertyBag(type = type.text))
        return FileImpl(file)
    }
}