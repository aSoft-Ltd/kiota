package kiota

import kiota.internal.ByteArrayFile
import kiota.internal.TextFile

class VirtualFileReader : FileReader {
    override suspend fun readBytes(file: File): ByteArray = when (file) {
        is ByteArrayFile -> file.content
        is TextFile -> file.content.encodeToByteArray()
        else -> throw IllegalArgumentException("Unsupported file type")
    }

    override suspend fun readText(file: File): String = when(file) {
        is ByteArrayFile -> file.content.decodeToString()
        is TextFile -> file.content
        else -> throw IllegalArgumentException("Unsupported file type")
    }
}