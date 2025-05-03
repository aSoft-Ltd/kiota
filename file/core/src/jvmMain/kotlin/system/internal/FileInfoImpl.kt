package system.internal

import system.FileInfo
import system.MemorySize
import system.MemoryUnit
import system.Multiplier
import java.io.File

class FileInfoImpl(override val file: LocalFileImpl) : FileInfo {
    private val separator = if (file.path.contains("\\")) "\\" else "/"
    override fun name(extension: Boolean): String {
        val full = file.path.substringAfterLast(separator)
        if (extension) return full
        val ext = extension()
        if (ext == "") return full
        return full.substringBefore(".$ext")
    }

    override fun extension(): String = file.path.split(".").lastOrNull() ?: ""

    override suspend fun size(): MemorySize = MemorySize(
        value = File(file.path).length().toDouble(),
        unit = MemoryUnit.Bytes,
        multiplier = Multiplier.Unit
    )
}