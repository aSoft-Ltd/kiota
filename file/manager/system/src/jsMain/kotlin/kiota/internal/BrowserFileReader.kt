package kiota.internal

import kiota.File
import kiota.readBytesOf
import koncurrent.Executor
import koncurrent.Later
import org.w3c.files.FileReader
import kiota.FileReader as MppFileReader

internal class BrowserFileReader : MppFileReader {

    val reader: FileReader = FileReader()

    override fun readBytes(file: File, executor: Executor): Later<ByteArray> {
        file as FileImpl
        return reader.readBytesOf(
            blob = file.wrapped,
            executor = executor,
            actionName = "Reading ${file.wrapped.name}",
            onAbortMessage = "File reading of ${file.wrapped.name} has been aborted",
            onErrorMessage = "Failed to read file: ${file.wrapped.name}"
        )
    }
}