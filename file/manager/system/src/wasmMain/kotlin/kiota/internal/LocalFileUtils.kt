package kiota.internal

import org.w3c.files.File
import kiota.File

internal fun kiota.File.toFile(): File = when(this) {
    is FileImpl -> wrapped
    is FileUrl -> throw IllegalArgumentException("Cannot convert FileUrl to File")
    else -> throw IllegalArgumentException("Unsupported file type: $this")
}