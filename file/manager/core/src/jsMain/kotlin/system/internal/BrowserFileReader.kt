package system.internal

import system.LocalFile
import system.readBytesOf
import koncurrent.Executor
import koncurrent.Later
import org.w3c.files.FileReader
import system.FileReader as MppFileReader

internal class BrowserFileReader : MppFileReader {

    val reader: FileReader = FileReader()

    override fun readBytes(file: LocalFile, executor: Executor): Later<ByteArray> {
        file as LocalFileImpl
        return reader.readBytesOf(
            blob = file.wrapped,
            executor = executor,
            actionName = "Reading ${file.wrapped.name}",
            onAbortMessage = "File reading of ${file.wrapped.name} has been aborted",
            onErrorMessage = "Failed to read file: ${file.wrapped.name}"
        )
    }
}