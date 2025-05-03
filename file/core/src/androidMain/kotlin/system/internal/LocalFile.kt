package system.internal

import android.net.Uri
import system.LocalFile

fun LocalFile(path: String): LocalFile = LocalFilePath(path)

fun LocalFile(uri: Uri): LocalFile = when (uri.scheme) {
    "content" -> LocalFileUri(uri)
    "file" -> LocalFilePath(uri.path ?: "")
    else -> throw IllegalArgumentException("Unsupported URI scheme: ${uri.scheme}")
}