package kiota

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kiota.file.PickerLimit
import kiota.file.SingleMediaPicker
import kiota.file.mime.Image
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime
import kiota.file.mime.Video
import kiota.file.response.toSingle
import kiota.file.toResponse
import kiota.internal.FileInfo
import kiota.internal.File

class AndroidSingleMediaPicker(private val activity: ComponentActivity) : SingleMediaPicker {
    private var scope: CoroutineScope? = null
    private var launcher: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private val results by lazy { Channel<Uri?>() }

    fun register() {
        if (launcher != null) return
        scope = CoroutineScope(SupervisorJob())
        launcher = activity.registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            scope?.launch { results.send(uri) }
        }
    }

    private suspend fun show(
        mimes: List<Mime>,
        limit: MemorySize,
    ): SingleFileResponse {
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(mimes.toMediaType())
            .build()
        l.launch(request)
        val files = listOf(results.receive()?.let { File(it) } ?: return Cancelled)
        val infos = files.map { FileInfo(activity, it) }
        return files.toResponse(mimes, PickerLimit(limit, 1), infos).toSingle()
    }

    override suspend fun open(
        mimes: List<MediaMime>,
        limit: MemorySize,
    ): SingleFileResponse {
        if (mimes.isEmpty()) return open(listOf(Image, Video), limit)
        return show(mimes, limit)
    }

    fun unregister() {
        scope?.cancel()
        scope = null
    }
}