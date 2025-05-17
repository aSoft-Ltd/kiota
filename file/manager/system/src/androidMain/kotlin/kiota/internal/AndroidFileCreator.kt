package kiota.internal

import android.content.Context
import kiota.FileCreator
import kiota.FileScope
import kiota.SingleFileResponse
import kiota.file.mime.Mime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class AndroidFileCreator(private val context: Context) : FileCreator {

    override suspend fun create(content: ByteArray, name: String, type: Mime) = save(name) {
        write(content)
    }

    override suspend fun create(content: String, name: String, type: Mime) = save(name) {
        val w = writer()
        w.append(content)
        w.flush()
        w.close()
    }

    private suspend fun save(
        name: String,
        block: OutputStream.() -> Unit
    ): SingleFileResponse {
        val dir = File(context.filesDir, "tmp")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, name)
        withContext(Dispatchers.IO) {
            file.createNewFile()
            val fos = FileOutputStream(file)
            fos.block()
            fos.flush()
            fos.close()
        }
        return File(file.path, FileScope.public)
    }
}