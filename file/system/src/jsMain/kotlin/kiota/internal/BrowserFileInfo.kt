package kiota.internal

import kiota.FileInfo
import kiota.MemorySize
import kiota.MemoryUnit
import kiota.Multiplier

class BrowserFileInfo(override val file: FileImpl) : FileInfo {

    override fun name(extension: Boolean): String {
        if (extension) return file.wrapped.name
        return if (ext.isNotEmpty()) file.wrapped.name.substringBeforeLast(".$ext") else file.wrapped.name
    }

    private val ext by lazy {
        file.wrapped.name.substringAfterLast(".", "")
    }

    override fun extension(): String = ext

    override suspend fun size(): MemorySize = MemorySize(
        value = file.wrapped.size.toDouble(),
        multiplier = Multiplier.Unit,
        unit = MemoryUnit.Bytes
    )
}