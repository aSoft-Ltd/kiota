package system.internal

import android.content.Context
import system.LocalFile

fun FileInfo(context: Context, file: LocalFile) = when (file) {
    is LocalFilePath -> FileInfoPath(file)
    is LocalFileUri -> FileInfoUri(context, file)
    else -> throw IllegalArgumentException("Unsupported LocalFile type: ${file::class.simpleName} on Android")
}