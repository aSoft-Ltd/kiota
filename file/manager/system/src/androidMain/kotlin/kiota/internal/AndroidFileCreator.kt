package kiota.internal

import android.content.Context
import android.os.Build
import kiota.Failure
import kiota.FileCreationExplanation
import kiota.FileCreationResult
import kiota.FileCreator
import kiota.InvalidFileName
import kiota.OutOfMemory
import kiota.file.mime.Mime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File as JFile
import java.io.FileOutputStream

class AndroidFileCreator(private val context: Context) : FileCreator {

    override suspend fun create(content: ByteArray, name: String, type: Mime) = save(name, content)

    override suspend fun create(content: String, name: String, type: Mime) = save(name, content.encodeToByteArray())

    private fun String.isValidFileName(): Boolean = !contains("/") && !contains("\\") && isNotBlank()

    private suspend fun save(
        name: String,
        content: ByteArray,
    ): FileCreationResult<FileCreationExplanation> {
        val reasons = mutableListOf<FileCreationExplanation>()
        if (!name.isValidFileName()) {
            reasons.add(InvalidFileName(name))
        }

        val dir = JFile(context.filesDir, "tmp")
        if (!dir.exists()) dir.mkdirs()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && context.filesDir.freeSpace <= content.size) {
            reasons.add(OutOfMemory)
        }

        if (reasons.isNotEmpty()) return Failure(reasons)

        val file = JFile(dir, name)
        withContext(Dispatchers.IO) {
            file.createNewFile()
            val fos = FileOutputStream(file)
            fos.write(content)
            fos.flush()
            fos.close()
        }
        return File(file.path)
    }
}