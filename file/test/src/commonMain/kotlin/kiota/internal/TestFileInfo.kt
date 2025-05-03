package kiota.internal

import kiota.FileInfo
import kiota.MemorySize
import kiota.MemoryUnit
import kiota.Multiplier

class TestFileInfo(override val file: TestFile) : FileInfo {

    override fun name(extension: Boolean): String {
        if (extension) return file.name
        val e = ext
        if (e.isEmpty()) return file.name
        return file.name.substringBeforeLast('.')
    }

    private val ext by lazy { file.name.substringAfterLast('.', "") }

    override fun extension(): String = ext

    override suspend fun size(): MemorySize {
        val size = when (file) {
            is ByteArrayFile -> file.content.size
            is TextFile -> file.content.encodeToByteArray().size
            else -> throw IllegalArgumentException("Unsupported file type")
        }
        return MemorySize(
            value = size.toDouble(),
            unit = MemoryUnit.Bytes,
            multiplier = Multiplier.Unit
        )
    }
}