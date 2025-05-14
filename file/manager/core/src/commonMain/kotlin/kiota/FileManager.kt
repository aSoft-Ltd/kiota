package kiota

import kiota.file.FilePickers

interface FileManager : FileOpener, FileCreator, FileReader, FileSaver, FileDeleter {
    val pickers: FilePickers<*, *, *, *>
    fun exists(file: File): Boolean
    fun info(file: File): FileInfo
}