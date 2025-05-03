package kiota.internal

import android.content.Context
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kiota.FileReader
import kiota.File
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.Charset

internal class AndroidFileReader(private val context: Context) : FileReader {

    override suspend fun readText(file: kiota.File): String = when (file) {
        is FilePath -> readText(file)
        is FileUri -> readText(file)
        else -> throw IllegalArgumentException("File of type `${file::class.simpleName}` is not supported on Android")
    }

    private suspend fun readText(file: FilePath) = withContext(Dispatchers.IO) {
        File(file.path).readText()
    }

    private suspend fun readText(file: FileUri) = withContext(Dispatchers.IO) {
        val baos = file.toByteArrayOutputStream()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            baos.toString(Charset.defaultCharset())
        } else {
            baos.toString()
        }
    }

    override suspend fun readBytes(file: kiota.File): ByteArray = when (file) {
        is FilePath -> readBytes(file)
        is FileUri -> readBytes(file)
        else -> throw IllegalArgumentException("File of type `${file::class.simpleName}` is not supported on Android")
    }

    private suspend fun readBytes(file: FilePath) = withContext(Dispatchers.IO) {
        File(file.path).readBytes()
    }

    private suspend fun readBytes(file: FileUri) = withContext(Dispatchers.IO) {
        file.toByteArrayOutputStream().toByteArray()
    }

    private fun FileUri.toByteArrayOutputStream(): ByteArrayOutputStream {
        val contentResolver = context.contentResolver
        val fis = contentResolver.openInputStream(uri) ?: throw IllegalArgumentException(
            "Failed to open file: $uri for reading"
        )
        val baos = ByteArrayOutputStream()
        fis.copyTo(baos)
        fis.close()
        baos.close()
        return baos
    }
}