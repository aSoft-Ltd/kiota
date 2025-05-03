package system.internal

import org.w3c.files.FileReader
import system.LocalFile
import system.readBytesOf
import system.readTextOf
import system.FileReader as MppFileReader

internal class BrowserFileReader : MppFileReader {

    override suspend fun readBytes(file: LocalFile): ByteArray = when (file) {
        is LocalFileImpl -> FileReader().readBytesOf(
            file.wrapped,
            onAbortMessage = "File reading of ${file.wrapped.name} has been aborted",
            onErrorMessage = "Failed to read file: ${file.wrapped.name}"
        ).also {
            println("Finished reading bytes")
        }

        else -> throw IllegalArgumentException("Unsupported file type: $file")
    }

    override suspend fun readText(file: LocalFile): String = when (file) {
        is LocalFileImpl -> FileReader().readTextOf(
            file.wrapped,
            onAbortMessage = "File reading of ${file.wrapped.name} has been aborted",
            onErrorMessage = "Failed to read file: ${file.wrapped.name}"
        )

        else -> throw IllegalArgumentException("Unsupported file type: $file")
    }
}