package kiota

import kiota.file.FilePickerFactory
import kiota.internal.BrowserFileCreator
import kiota.internal.BrowserFileDeleter
import kiota.internal.BrowserFileExposer
import kiota.internal.BrowserFileInfo
import kiota.internal.BrowserFileOpener
import kiota.internal.BrowserFileReader
import kiota.internal.FileImpl

class BrowserFileManager :
    FileManager,
    FileReader by BrowserFileReader(),
    FileCreator by BrowserFileCreator(),
    FileDeleter by BrowserFileDeleter(),
    FileOpener by BrowserFileOpener(),
    FileExposer by BrowserFileExposer(),
    FilePickerFactory by BrowserFilePickerFactory() {

    override fun exists(file: File): Boolean = true

    override fun info(file: File): FileInfo = when (file) {
        is FileImpl -> BrowserFileInfo(file)
        else -> throw IllegalArgumentException("Unsupported file type: $file")
    }

    override fun canShare(): Boolean = false
}