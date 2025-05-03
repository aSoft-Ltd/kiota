package kiota

import kiota.file.mime.Application
import kiota.file.mime.Mime
import kiota.file.mime.Text

interface FileCreator {
    /**
     * Saves the file with [content] to the file system
     *
     * @return [SingleFileResponse] when the file was successfully saved
     */
    suspend fun create(
        content: ByteArray,
        name: String = "file.bin",
        type: Mime = Application.OctetStream
    ): SingleFileResponse

    suspend fun create(
        content: String,
        name: String = "file.txt",
        type: Mime = Text.Plain
    ): SingleFileResponse
}