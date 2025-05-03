package kiota

import kase.progress.ProgressBus
import kase.progress.VoidProgressBus
import kollections.component1
import koncurrent.Executor
import koncurrent.Later
import koncurrent.PendingLater
import kotlinx.coroutines.suspendCancellableCoroutine
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.files.Blob
import org.w3c.files.FileReader
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

inline fun ArrayBuffer.toByteArray(): ByteArray {
    val array = Int8Array(this)
    return ByteArray(array.length) { array[it] }
}

fun FileReader.readBytesOf(
    bus: ProgressBus = VoidProgressBus,
    blob: Blob,
    executor: Executor,
    actionName: String,
    onAbortMessage: String,
    onErrorMessage: String
): Later<ByteArray> {
    val later = PendingLater<ByteArray>(executor)
    val (reading) = bus.setStages(actionName)
    onprogress = {
        bus.updateProgress(reading(it.loaded.toDouble().toLong(), it.total.toDouble().toLong()))
    }
    onabort = {
        later.rejectWith(IllegalStateException(onAbortMessage))
    }
    onerror = {
        later.rejectWith(IllegalArgumentException(onErrorMessage))
    }
    onloadend = {
        when (val res = result) {
            is ArrayBuffer -> later.resolveWith(res.toByteArray())
            else -> later.rejectWith(IllegalStateException("Failed to read bytes"))
        }
    }
    readAsArrayBuffer(blob)
    return later
}

suspend fun FileReader.readBytesOf(
    blob: Blob,
    onAbortMessage: String,
    onErrorMessage: String,
): ByteArray = suspendCancellableCoroutine { cont ->
    onprogress = {
        val done = it.loaded.toDouble().toLong()
        val total = it.total.toDouble().toLong()
        // TODO("Update progress: $done / $total")
    }
    onabort = {
        cont.resumeWithException(IllegalStateException(onAbortMessage))
    }
    onerror = {
        cont.resumeWithException(IllegalArgumentException(onErrorMessage))
    }
    onloadend = {
        when (val res = result) {
            is ArrayBuffer -> cont.resume(res.toByteArray())
            else -> cont.resumeWithException(IllegalStateException("Failed to read bytes"))
        }
    }
    readAsArrayBuffer(blob)
}

suspend fun FileReader.readTextOf(
    blob: Blob,
    onAbortMessage: String,
    onErrorMessage: String,
): String = suspendCancellableCoroutine { cont ->
    onprogress = {
        val done = it.loaded.toDouble().toLong()
        val total = it.total.toDouble().toLong()
        // TODO("Update progress: $done / $total")
    }
    onabort = {
        cont.resumeWithException(IllegalStateException(onAbortMessage))
    }
    onerror = {
        cont.resumeWithException(IllegalArgumentException(onErrorMessage))
    }
    onloadend = {
        when (val res = result) {
            is JsString -> cont.resume(res.toString())
            else -> cont.resumeWithException(IllegalStateException("Failed to read bytes"))
        }
    }
    readAsText(blob)
}

fun FileReader.readDataUrl(
    bus: ProgressBus = VoidProgressBus,
    blob: Blob,
    executor: Executor,
    actionName: String,
    onAbortMessage: String,
    onErrorMessage: String
): Later<String> {
    val later = PendingLater<String>(executor)
    val (reading) = bus.setStages(actionName)
    onprogress = {
        val pr = reading(it.loaded.toDouble().toLong(), it.total.toDouble().toLong())
        bus.updateProgress(pr)
    }
    onabort = {
        later.rejectWith(IllegalStateException(onAbortMessage))
    }
    onerror = {
        later.rejectWith(IllegalArgumentException(onErrorMessage))
    }
    onloadend = {
        when (result) {
            is JsString -> later.resolveWith(result.toString())
            else -> later.rejectWith(IllegalStateException("Failed to read data URL"))
        }
    }
    readAsDataURL(blob)
    return later
}