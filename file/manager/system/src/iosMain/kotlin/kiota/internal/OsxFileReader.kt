@file:OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)

package kiota.internal

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.stringWithContentsOfURL
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.UTTypePlainText
import platform.UniformTypeIdentifiers.loadDataRepresentationForContentType
import kiota.FileReader
import kiota.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class OsxFileReader : FileReader {
    override suspend fun readBytes(file: File): ByteArray = when (file) {
        is FileUrl -> readBytes(file)
        is FileProvider -> readBytes(file)
        else -> throw IllegalArgumentException("Unsupported file type on IOS")
    }

    private fun readBytes(file: FileUrl): ByteArray {
        val data = NSData.dataWithContentsOfURL(file.url) ?: return byteArrayOf()
        val len = data.length.toInt()
        return data.bytes()?.readBytes(len) ?: byteArrayOf()
    }

    private suspend fun readBytes(file: FileProvider): ByteArray {
        val identifier = file.provider.registeredTypeIdentifiers.firstOrNull() ?: throw RuntimeException("No identifier found")
        val type = UTType.typeWithIdentifier(identifier as String) ?: throw RuntimeException("No type found")
        return suspendCancellableCoroutine { cont ->
            file.provider.loadDataRepresentationForContentType(type) { data, error ->
                if (error != null) {
                    cont.resumeWithException(RuntimeException(error.localizedDescription))
                } else {
                    val len = data?.length()?.toInt() ?: 0
                    cont.resume(data?.bytes()?.readBytes(len) ?: ByteArray(0))
                }
            }
        }
    }

    override suspend fun readText(file: File): String = when(file) {
        is FileUrl -> readText(file)
        is FileProvider -> readText(file)
        else -> throw IllegalArgumentException("Unsupported file type on IOS")
    }

    private fun readText(file: FileUrl): String {
        return NSString.stringWithContentsOfURL(file.url)?.toString() ?: ""
    }

    private suspend fun readText(file: FileProvider): String = suspendCancellableCoroutine { cont ->
        file.provider.loadItemForTypeIdentifier(UTTypePlainText.identifier, null) { data, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else {
                val str = NSString.create(data = data as NSData, encoding = NSUTF8StringEncoding)
                cont.resume(str.toString())
            }
        }
    }
}