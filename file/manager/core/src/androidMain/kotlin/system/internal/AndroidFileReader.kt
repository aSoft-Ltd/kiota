package system.internal

import android.content.Context
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import system.FileReader
import system.LocalFile
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.charset.Charset

internal class AndroidFileReader(private val context: Context) : FileReader {

    override suspend fun readText(file: LocalFile): String = when (file) {
        is LocalFilePath -> readText(file)
        is LocalFileUri -> readText(file)
        else -> throw IllegalArgumentException("LocalFile of type `${file::class.simpleName}` is not supported on Android")
    }

    private suspend fun readText(file: LocalFilePath) = withContext(Dispatchers.IO) {
        File(file.path).readText()
    }

    private suspend fun readText(file: LocalFileUri) = withContext(Dispatchers.IO) {
        val baos = file.toByteArrayOutputStream()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            baos.toString(Charset.defaultCharset())
        } else {
            baos.toString()
        }
    }

    override suspend fun readBytes(file: LocalFile): ByteArray = when (file) {
        is LocalFilePath -> readBytes(file)
        is LocalFileUri -> readBytes(file)
        else -> throw IllegalArgumentException("LocalFile of type `${file::class.simpleName}` is not supported on Android")
    }

    private suspend fun readBytes(file: LocalFilePath) = withContext(Dispatchers.IO) {
        File(file.path).readBytes()
    }

    private suspend fun readBytes(file: LocalFileUri) = withContext(Dispatchers.IO) {
        file.toByteArrayOutputStream().toByteArray()
    }

    private fun LocalFileUri.toByteArrayOutputStream(): ByteArrayOutputStream {
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