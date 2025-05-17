package kiota.internal

import kiota.File
import kiota.FileInfo
import kiota.FileInformer
import platform.Foundation.NSFileManager

internal class OsxFileInformer : FileInformer {
    
    override fun exists(file: File): Boolean = when (file) {
        is FileUrl -> file.url.path?.let { NSFileManager.defaultManager.fileExistsAtPath(it) } ?: false
        is FileProvider -> true
        else -> false
    }

    override fun info(file: File): FileInfo = when (file) {
        is FileUrl -> FileInfoPath(file)
        is FileProvider -> FileInfoProvider(file)
        else -> throw IllegalArgumentException("Unsupported file type on IOS")
    }

    override fun canShare(): Boolean = true
}