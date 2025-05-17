package kiota

import kiota.file.FilePickerFactory
import kiota.internal.TestFile
import kiota.internal.TestFileInfo

class VirtualFileManager(
    private val files: MutableMap<String, File> = mutableMapOf()
) : FileManager,
    FileExposer,
    FileDeleter,
    FileCreator by VirtualFileCreator(files),
    FileOpener by VirtualFileOpener(files),
    FileReader by VirtualFileReader(),
    FilePickerFactory by VirtualFilePickerFactory(files) {

    override fun exists(file: File): Boolean = files.containsValue(file)

    override fun info(file: File): FileInfo = when (file) {
        is TestFile -> TestFileInfo(file)
        else -> throw IllegalArgumentException("Unsupported file type")
    }

    override suspend fun export(file: File): SingleFileResponse = file
    override suspend fun share(file: File): SingleFileResponse = file

    override fun canShare(): Boolean = false

    override fun delete(file: File): Boolean {
        if (!files.containsValue(file)) return false
        val entry = files.entries.find { it.value == file } ?: return false
        return files.remove(entry.key) == file
    }
}