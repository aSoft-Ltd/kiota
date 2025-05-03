package kiota.internal

import android.net.Uri
import kiota.File

fun File(path: String): File = FilePath(path)

fun File(uri: Uri): File = when (uri.scheme) {
    "content" -> FileUri(uri)
    "file" -> FilePath(uri.path ?: "")
    else -> throw IllegalArgumentException("Unsupported URI scheme: ${uri.scheme}")
}