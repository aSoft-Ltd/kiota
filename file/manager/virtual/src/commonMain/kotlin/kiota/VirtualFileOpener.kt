package kiota

class VirtualFileOpener(private val files: MutableMap<String, File>) : FileOpener {
    override suspend fun open(file: File): SingleFileResponse = file

    override suspend fun open(url: String): SingleFileResponse = files[url] ?: Failure(reasons = listOf())
}