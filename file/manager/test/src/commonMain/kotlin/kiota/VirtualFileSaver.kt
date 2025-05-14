package kiota

class VirtualFileSaver(private val files: MutableMap<String, File>) : FileSaver {
    override suspend fun saveAs(file: File): SingleFileResponse = file
}