package system

import kase.progress.ProgressBus
import kase.progress.VoidProgressBus
import kollections.component1
import koncurrent.Executor
import koncurrent.Later
import koncurrent.PendingLater
import koncurrent.rejectWith
import koncurrent.resolveWith
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.khronos.webgl.get
import org.w3c.files.Blob
import org.w3c.files.FileReader

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
        bus.updateProgress(reading(it.loaded.toLong(), it.total.toLong()))
    }
    onabort = {
        later.rejectWith(IllegalStateException(onAbortMessage))
    }
    onerror = {
        later.rejectWith(IllegalArgumentException(onErrorMessage))
    }
    onloadend = {
        val result = result.unsafeCast<ArrayBuffer>()
        later.resolveWith(result.toByteArray())
    }
    readAsArrayBuffer(blob)
    return later
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
        bus.updateProgress(reading(it.loaded.toLong(), it.total.toLong()))
    }
    onabort = {
        later.rejectWith(IllegalStateException(onAbortMessage))
    }
    onerror = {
        later.rejectWith(IllegalArgumentException(onErrorMessage))
    }
    onloadend = {
        later.resolveWith(result.unsafeCast<String>())
    }
    readAsDataURL(blob)
    return later
}