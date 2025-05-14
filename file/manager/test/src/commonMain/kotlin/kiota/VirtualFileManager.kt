package kiota

import kiota.file.FilePickers
import kiota.internal.TestFile
import kiota.internal.TestFileInfo

class VirtualFileManager(
    private val files: MutableMap<String, File> = mutableMapOf()
) : FileManager,
    FileSaver,
    FileDeleter,
    FileCreator by VirtualFileCreator(files),
    FileOpener by VirtualFileOpener(files),
    FileReader by VirtualFileReader() {
    override val pickers: FilePickers<*, *, *, *> by lazy {
        FilePickers(
            documents = VirtualMultiFilePicker(files),
            document = VirtualSingleFilePicker(files),
            medias = VirtualMultiMediaPicker(files),
            media = VirtualSingleMediaPicker(files),
        )
    }

    override fun exists(file: File): Boolean = files.containsValue(file)

    override fun info(file: File): FileInfo = when (file) {
        is TestFile -> TestFileInfo(file)
        else -> throw IllegalArgumentException("Unsupported file type")
    }

    override suspend fun saveAs(file: File): SingleFileResponse = file

    override fun delete(file: File): Boolean {
        if (!files.containsValue(file)) return false
        val entry = files.entries.find { it.value == file } ?: return false
        return files.remove(entry.key) == file
    }
}