package kiota.internal

import android.content.Context
import kiota.File

fun FileInfo(context: Context, file: File) = when (file) {
    is FilePath -> FileInfoPath(file)
    is FileUri -> FileInfoUri(context, file)
    else -> throw IllegalArgumentException("Unsupported File type: ${file::class.simpleName} on Android")
}