package kiota.internal

import kiota.FileDeleter

internal class JvmFileDeleter : FileDeleter {
    override fun delete(file: kiota.File): Boolean {
        val f = file.toFileOrNull() ?: return false
        if (!f.exists()) return false
        return f.delete()
    }
}