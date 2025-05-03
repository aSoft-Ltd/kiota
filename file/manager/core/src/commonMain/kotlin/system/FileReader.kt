package system

interface FileReader {
    suspend fun readBytes(file: LocalFile): ByteArray
    suspend fun readText(file: LocalFile): String
}