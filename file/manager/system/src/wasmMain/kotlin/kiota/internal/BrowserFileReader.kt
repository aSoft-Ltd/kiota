package kiota.internal

import org.w3c.files.FileReader
import kiota.File
import kiota.readBytesOf
import kiota.readTextOf
import kiota.FileReader as MppFileReader

internal class BrowserFileReader : MppFileReader {

    override suspend fun readBytes(file: File): ByteArray = when (file) {
        is FileImpl -> FileReader().readBytesOf(
            file.wrapped,
            onAbortMessage = "File reading of ${file.wrapped.name} has been aborted",
            onErrorMessage = "Failed to read file: ${file.wrapped.name}"
        ).also {
            println("Finished reading bytes")
        }

        else -> throw IllegalArgumentException("Unsupported file type: $file")
    }

    override suspend fun readText(file: File): String = when (file) {
        is FileImpl -> FileReader().readTextOf(
            file.wrapped,
            onAbortMessage = "File reading of ${file.wrapped.name} has been aborted",
            onErrorMessage = "Failed to read file: ${file.wrapped.name}"
        )

        else -> throw IllegalArgumentException("Unsupported file type: $file")
    }
}