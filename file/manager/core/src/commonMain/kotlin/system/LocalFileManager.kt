package system

import system.file.FilePickers

interface LocalFileManager : FileOpener, FileSaver, FileReader {
    val pickers: FilePickers<*, *, *, *>
    fun exists(file: LocalFile): Boolean
    fun info(file: LocalFile): FileInfo
}