package kiota

class VirtualFileExposer(private val files: MutableMap<String, File>) : FileExposer {
    override suspend fun export(file: File): SingleFileResponse = file
    override suspend fun share(file: File): SingleFileResponse = file
}