package kiota.internal

import kiota.File
import java.io.File as JvmFile

internal fun File.toPath(): String = when (this) {
    is FileImpl -> path
    else -> throw IllegalArgumentException("Unsupported file type: $this")
}

internal fun File.toPathOrNull(): String? = try {
    toPath()
} catch (e: IllegalArgumentException) {
    null
}

internal fun File.toFile() = JvmFile(toPath())

internal fun File.toFileOrNull(): JvmFile? = try {
    toFile()
} catch (e: IllegalArgumentException) {
    null
}