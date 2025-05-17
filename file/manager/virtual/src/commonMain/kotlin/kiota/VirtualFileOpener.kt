package kiota

class VirtualFileOpener(private val files: MutableMap<String, File>) : FileOpener {
    override suspend fun open(file: File) = file

    override suspend fun open(url: String): FileOpenResult<FileOpenExplanation> = files[url] ?: Failure(emptyList())
}