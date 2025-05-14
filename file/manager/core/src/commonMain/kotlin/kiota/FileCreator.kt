package kiota

import kiota.file.mime.Application
import kiota.file.mime.Mime
import kiota.file.mime.Text

interface FileCreator {
    /**
     * Creates a file with [content] inside the private/app directory
     *
     * @return [SingleFileResponse] when the file was successfully created
     */
    suspend fun create(
        content: ByteArray,
        name: String = "file.bin",
        type: Mime = Application.OctetStream
    ): SingleFileResponse


    /**
     * Creates a file with [content] inside the private/app directory
     *
     * @return [SingleFileResponse] when the file was successfully created
     */
    suspend fun create(
        content: String,
        name: String = "file.txt",
        type: Mime = Text.Plain
    ): SingleFileResponse
}