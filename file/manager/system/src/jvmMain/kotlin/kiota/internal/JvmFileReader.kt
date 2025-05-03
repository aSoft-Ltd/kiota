package kiota.internal

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kiota.FileReader
import kiota.File

internal class JvmFileReader : FileReader {
    override suspend fun readBytes(file: File): ByteArray = withContext(Dispatchers.IO) {
        file.toFile().readBytes()
    }

    override suspend fun readText(file: File): String = withContext(Dispatchers.IO) {
        file.toFile().readText()
    }
}