@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package system.internal

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.Foundation.NSNumber
import platform.Foundation.NSURLFileSizeKey
import system.FileInfo
import system.MemorySize
import system.MemoryUnit
import system.Multiplier

class LocalFileInfoPath(override val file: LocalFileUrl) : FileInfo {
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