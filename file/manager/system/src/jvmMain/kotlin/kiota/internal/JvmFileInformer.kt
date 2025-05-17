package kiota.internal

import kiota.File
import kiota.FileInfo
import kiota.FileInformer

class JvmFileInformer : FileInformer {

    override fun exists(file: File): Boolean = file.toFileOrNull()?.exists() ?: false

    override fun info(file: File): FileInfo = when (file) {
        is FileImpl -> FileInfoImpl(file)
        else -> throw IllegalArgumentException("Unknown file type: $file")
    }

    override fun canShare(): Boolean = false
}