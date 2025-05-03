package system.internal

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.loadDataRepresentationForContentType
import system.FileInfo
import system.MemorySize
import system.MemoryUnit
import system.Multiplier
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocalFileInfoProvider(override val file: LocalFileProvider) : FileInfo {
    private val ext by lazy {
        val identifier = file.provider.registeredTypeIdentifiers.firstOrNull() ?: return@lazy ""
        UTType.typeWithIdentifier(identifier as String)?.toString()?.split(".")?.lastOrNull() ?: ""
    }

    private val n by lazy { file.provider.suggestedName ?: "" }

    override fun name(extension: Boolean): String = if (extension && ext.isNotEmpty()) {
        "$n.$ext"
    } else {
        n
    }

    override fun extension(): String = ext

    private var cached: MemorySize? = null

    override suspend fun size(): MemorySize {
        val identifier = file.provider.registeredTypeIdentifiers.firstOrNull() ?: return MemorySize.Zero
        val type = UTType.typeWithIdentifier(identifier as String) ?: return MemorySize.Zero
        return suspendCancellableCoroutine { cont ->
            file.provider.loadDataRepresentationForContentType(type) { data, error ->
                if (error != null) {
                    return@loadDataRepresentationForContentType cont.resumeWithException(RuntimeException(error.localizedDescription))
                }
                val m = MemorySize(
                    value = data?.length()?.toDouble() ?: 0.0,
                    unit = MemoryUnit.Bytes,
                    multiplier = Multiplier.Unit
                )
                cached = m
                cont.resume(m)
            }
        }
    }
}