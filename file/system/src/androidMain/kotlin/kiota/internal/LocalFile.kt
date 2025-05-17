package kiota.internal

import android.net.Uri
import kiota.File
import kiota.FileScope

fun File(path: String, scope: FileScope): File = FilePath(path, scope)

fun File(uri: Uri, scope: FileScope): File = when (uri.scheme) {
    "content" -> FileUri(uri, scope)
    "file" -> FilePath(uri.path ?: "", scope)
    else -> throw IllegalArgumentException("Unsupported URI scheme: ${uri.scheme}")
}