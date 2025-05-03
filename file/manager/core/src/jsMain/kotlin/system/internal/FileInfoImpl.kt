package system.internal

import system.FileInfo
import system.MemorySize
import system.MemoryUnit
import system.Multiplier

internal class FileInfoImpl(override val file: LocalFileImpl) : FileInfo {

    override fun name(extension: Boolean): String {
        if (extension) return file.wrapped.name
        return if (ext.isNotEmpty()) file.wrapped.name.substringBeforeLast(".$ext") else file.wrapped.name
    }

    private val ext by lazy {
        file.wrapped.name.substringAfterLast(".", "")
    }

    override fun extension(): String = ext

    override fun size(): MemorySize = MemorySize(
        value = file.wrapped.size.toDouble(),
        multiplier = Multiplier.Unit,
        unit = MemoryUnit.Bytes
    )
}