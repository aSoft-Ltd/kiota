package system.internal

import org.w3c.files.File
import system.LocalFile

internal fun LocalFile.toFile(): File = when(this) {
    is LocalFileImpl -> wrapped
    is LocalFileUrl -> throw IllegalArgumentException("Cannot convert LocalFileUrl to File")
    else -> throw IllegalArgumentException("Unsupported file type: $this")
}