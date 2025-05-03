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
import kiota.file.MultiMediaPicker
import kiota.file.PickerLimit
import kiota.file.mime.Image
import kiota.file.mime.MediaMime
import kiota.file.mime.Video
import kiota.file.toResponse
import kiota.internal.FileInfo
import kiota.internal.File

class AndroidMultiMediaPicker(private val activity: ComponentActivity) : MultiMediaPicker {
    private var scope: CoroutineScope? = null
    private var launcher: ActivityResultLauncher<PickVisualMediaRequest>? = null
    private val results by lazy { Channel<List<Uri>>() }

    fun register() {
        if (launcher != null) return
        scope = CoroutineScope(SupervisorJob())
        launcher = activity.registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris ->
            scope?.launch { results.send(uris) }
        }
    }

    override suspend fun open(
        mimes: List<MediaMime>,
        limit: PickerLimit,
    ): MultiPickerResponse {
        if (mimes.isEmpty()) return open(listOf(Image, Video), limit)
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(mimes.toMediaType())
            .setMaxItems(limit.count)
            .build()
        l.launch(request)
        val files = results.receive().map { File(it) }
        val infos = files.map { FileInfo(activity, it) }
        return files.toResponse(mimes, limit, infos)
    }

    fun unregister() {
        scope?.cancel()
        scope = null
    }
}