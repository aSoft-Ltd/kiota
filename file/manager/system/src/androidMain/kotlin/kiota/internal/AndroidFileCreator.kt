package kiota.internal

import android.content.Context
import android.os.Build
import kiota.FileCreationResult
import kiota.FileCreator
import kiota.FileScope
import kiota.InvalidFileName
import kiota.OutOfMemory
import kiota.file.mime.Mime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class AndroidFileCreator(private val context: Context) : FileCreator {

    override suspend fun create(content: ByteArray, name: String, type: Mime) = save(name, content)

    override suspend fun create(content: String, name: String, type: Mime) = save(name, content.encodeToByteArray())

    private fun String.isValidFileName(): Boolean = !contains("/") && !contains("\\") && isNotBlank()

    private suspend fun save(
        name: String,
        content: ByteArray,
    ): FileCreationResult {
        if (name.isValidFileName()) return InvalidFileName(name)

        val dir = File(context.filesDir, "tmp")
        if (!dir.exists()) dir.mkdirs()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && context.filesDir.freeSpace <= content.size) {
            return OutOfMemory
        }

        val file = File(dir, name)
        withContext(Dispatchers.IO) {
            file.createNewFile()
            val fos = FileOutputStream(file)
            fos.write(content)
            fos.flush()
            fos.close()
        }
        return File(file.path, FileScope.public)
    }
}