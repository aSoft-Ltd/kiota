package kiota

import kiota.file.mime.Application
import kiota.file.mime.Mime
import kiota.file.mime.Text

/**
 * An interface to help create files in the private/sandbox directory
 */
interface FileCreator {
    /**
     * Creates a file with [content] inside the private/sandbox directory
     *
     * @return [FileCreationResult]
     */
    suspend fun create(
        content: ByteArray,
        name: String = "file.bin",
        type: Mime = Application.OctetStream
    ): FileCreationResult


    /**
     * Creates a file with [content] inside the private/private directory
     *
     * @return [FileCreationResult]
     */
    suspend fun create(
        content: String,
        name: String = "file.txt",
        type: Mime = Text.Plain
    ): FileCreationResult
}