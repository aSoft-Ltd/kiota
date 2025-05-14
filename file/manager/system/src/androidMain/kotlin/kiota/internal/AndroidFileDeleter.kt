package kiota.internal

import kiota.File
import kiota.FileDeleter

class AndroidFileDeleter : FileDeleter {
    override fun delete(file: File): Boolean {
        val f = file as? FilePath ?: return false
        val cand = java.io.File(file.path)
        if (!cand.exists()) return false
        return cand.delete()
    }
}