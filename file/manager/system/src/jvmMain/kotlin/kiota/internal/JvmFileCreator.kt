package kiota.internal

import kiota.FileCreator
import kiota.SingleFileResponse
import kiota.file.mime.Mime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class JvmFileCreator(private val sandbox: File) : FileCreator {

    private suspend fun file(name: String, block: File.() -> Unit): SingleFileResponse {
        if (!sandbox.exists()) sandbox.mkdirs()
        val directory = sandbox
        return withContext(Dispatchers.IO) {
            val file = File(directory, name).apply { createNewFile() }
            block(file)
            FileImpl(file.path)
        }
    }

    override suspend fun create(content: ByteArray, name: String, type: Mime) = file(name) { writeBytes(content) }

    override suspend fun create(content: String, name: String, type: Mime) = file(name) { writeText(content) }
}