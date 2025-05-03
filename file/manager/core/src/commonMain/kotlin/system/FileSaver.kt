package system

import system.file.mime.Application
import system.file.mime.Mime
import system.file.mime.Text

interface FileSaver {
    /**
     * Saves the file with [content] to the file system
     *
     * @return [SingleFileResponse] when the file was successfully saved
     */
    suspend fun save(
        content: ByteArray = byteArrayOf(),
        name: String = "file.bin",
        type: Mime = Application.OctetStream
    ): SingleFileResponse

    suspend fun save(
        content: String = "",
        name: String = "file.txt",
        type: Mime = Text.Plain
    ): SingleFileResponse
}