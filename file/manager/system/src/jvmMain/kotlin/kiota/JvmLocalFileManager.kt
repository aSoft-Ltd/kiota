package kiota

import kiota.file.FilePickers
import kiota.internal.FileImpl
import kiota.internal.FileInfoImpl
import kiota.internal.JvmFileCreator
import kiota.internal.JvmFileDeleter
import kiota.internal.JvmFileOpener
import kiota.internal.JvmFileReader
import kiota.internal.JvmFileSaver
import kiota.internal.toFileOrNull

class JvmFileManager(private val sandbox: java.io.File = java.io.File("./tmp")) :
    FileManager,
    FileReader by JvmFileReader(),
    FileCreator by JvmFileCreator(sandbox),
    FileDeleter by JvmFileDeleter(),
    FileSaver by JvmFileSaver(),
    FileOpener by JvmFileOpener() {
    override val pickers by lazy {
        FilePickers(
            documents = JvmMultiFilePicker(),
            document = JvmSingleFilePicker(),
            medias = JvmMultiMediaPicker(),
            media = JvmSingleMediaPicker(),
        )
    }

    override fun exists(file: File): Boolean = file.toFileOrNull()?.exists() ?: false

    override fun info(file: File): FileInfo = when (file) {
        is FileImpl -> FileInfoImpl(file)
        else -> throw IllegalArgumentException("Unknown file type: $file")
    }
}