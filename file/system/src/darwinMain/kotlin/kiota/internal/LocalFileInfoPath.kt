@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package kiota.internal

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSNumber
import platform.Foundation.NSURLFileSizeKey
import kiota.FileInfo
import kiota.MemorySize
import kiota.MemoryUnit
import kiota.Multiplier

class FileInfoPath(override val file: FileUrl) : FileInfo {
    override fun name(extension: Boolean): String {
        val n = file.url.lastPathComponent ?: ""
        if (extension) return n
        return n.substringAfterLast(".", "")
    }

    override fun extension(): String = file.url.lastPathComponent?.substringAfterLast('.', "") ?: ""

    override suspend fun size(): MemorySize {
        var size: NSNumber?
        memScoped {
            val fs = alloc<ObjCObjectVar<Any?>>()
            file.url.getResourceValue(forKey = NSURLFileSizeKey, value = fs.ptr, error = null)
            size = fs.value as? NSNumber
        }

        return MemorySize(
            value = size?.doubleValue ?: 0.0,
            unit = MemoryUnit.Bytes,
            multiplier = Multiplier.Unit
        )
    }
}