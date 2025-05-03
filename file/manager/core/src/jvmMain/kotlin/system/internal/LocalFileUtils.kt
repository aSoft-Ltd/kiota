package system.internal

import system.LocalFile
import java.io.File

internal fun LocalFile.toPath(): String = when (this) {
    is LocalFileImpl -> path
    else -> throw IllegalArgumentException("Unsupported file type: $this")
}

internal fun LocalFile.toPathOrNull(): String? = try {
    toPath()
} catch (e: IllegalArgumentException) {
    null
}

internal fun LocalFile.toFile() = File(toPath())

internal fun LocalFile.toFileOrNull(): File? = try {
    toFile()
} catch (e: IllegalArgumentException) {
    null
}