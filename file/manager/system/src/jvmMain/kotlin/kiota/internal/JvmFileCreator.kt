package kiota.internal

import kiota.InvalidFileNameError
import kiota.FileCreationResult
import kiota.FileCreator
import kiota.OutOfMemoryError
import kiota.file.mime.Mime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class JvmFileCreator(private val sandbox: File) : FileCreator {
    private fun String.isValidFileName(): Boolean = !contains("/") && !contains("\\") && isNotBlank()

    private suspend fun create(name: String, content: ByteArray): FileCreationResult {
        if (!name.isValidFileName()) return InvalidFileNameError(name)

        if (!sandbox.exists()) sandbox.mkdirs()
        val directory = sandbox
        if (directory.freeSpace <= content.size) return OutOfMemoryError

        return withContext(Dispatchers.IO) {
            val file = File(directory, name).apply { createNewFile() }
            file.writeBytes(content)
            FileImpl(file.path)
        }
    }

    override suspend fun create(content: ByteArray, name: String, type: Mime) = create(name, content)

    override suspend fun create(content: String, name: String, type: Mime) = create(name, content.encodeToByteArray())
}