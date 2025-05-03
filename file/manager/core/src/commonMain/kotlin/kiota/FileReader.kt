package kiota

interface FileReader {
    suspend fun readBytes(file: File): ByteArray
    suspend fun readText(file: File): String
}