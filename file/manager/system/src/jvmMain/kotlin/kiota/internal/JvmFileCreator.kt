package kiota.internal

import kiota.Failure
import kiota.FileCreationExplanation
import kiota.FileCreationResult
import kiota.FileCreator
import kiota.InvalidFileName
import kiota.OutOfMemory
import kiota.file.mime.Mime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class JvmFileCreator(private val sandbox: File) : FileCreator {
    private fun String.isValidFileName(): Boolean = !contains("/") && !contains("\\") && isNotBlank()

    private suspend fun create(name: String, content: ByteArray): FileCreationResult<FileCreationExplanation> {
        val errors = mutableListOf<FileCreationExplanation>()
        if (!name.isValidFileName()) {
            errors.add(InvalidFileName(name))
        }

        if (!sandbox.exists()) sandbox.mkdirs()
        val directory = sandbox
        if (directory.freeSpace <= content.size) {
            errors.add(OutOfMemory)
        }

        if (errors.isNotEmpty()) return Failure(errors)

        return withContext(Dispatchers.IO) {
            val file = File(directory, name).apply { createNewFile() }
            file.writeBytes(content)
            FileImpl(file.path)
        }
    }

    override suspend fun create(content: ByteArray, name: String, type: Mime) = create(name, content)

    override suspend fun create(content: String, name: String, type: Mime) = create(name, content.encodeToByteArray())
}