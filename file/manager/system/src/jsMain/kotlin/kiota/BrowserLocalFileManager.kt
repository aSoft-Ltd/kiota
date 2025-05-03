package kiota

import kiota.file.FilePickers
import kiota.internal.BrowserFileInfo
import kiota.internal.BrowserFileOpener
import kiota.internal.BrowserFileReader
import kiota.internal.BrowserFileCreator
import kiota.internal.FileImpl
import kiota.FileReader as MppFileReader

class BrowserFileManager :
    FileManager,
    MppFileReader by BrowserFileReader(),
    FileCreator by BrowserFileCreator(),
    FileOpener by BrowserFileOpener() {

    override val pickers by lazy {
        FilePickers(
            documents = BrowserMultiFilePicker(),
            document = BrowserSingleFilePicker(),
            medias = BrowserMultiMediaPicker(),
            media = BrowserSingleMediaPicker()
        )
    }

    override fun exists(file: File): Boolean = true

    override fun info(file: File): FileInfo = when (file) {
        is FileImpl -> BrowserFileInfo(file)
        else -> throw IllegalArgumentException("Unsupported file type: $file")
    }
}