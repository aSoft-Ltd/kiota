package kiota.internal

import kiota.FileInfo
import kiota.FileScope
import kiota.MemorySize
import kiota.MemoryUnit
import kiota.Multiplier
import java.io.File

class FileInfoImpl(override val file: FileImpl) : FileInfo {
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

    override fun scope(): FileScope = FileScope.public
}