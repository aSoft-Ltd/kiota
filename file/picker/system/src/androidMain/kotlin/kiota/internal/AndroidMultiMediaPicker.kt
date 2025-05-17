package kiota.internal

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import kiota.MultiPickingResult
import kiota.PickingExplanation
import kiota.file.PickerLimit
import kiota.file.mime.Image
import kiota.file.mime.MediaMime
import kiota.file.mime.Video
import kiota.file.toResult
import kiota.toMediaType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal class AndroidMultiMediaPicker(private val activity: ComponentActivity) {
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

    suspend fun open(
        mimes: Collection<MediaMime>,
        limit: PickerLimit,
    ): MultiPickingResult<PickingExplanation> {
        if (mimes.isEmpty()) return open(listOf(Image, Video), limit)
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(mimes.toMediaType())
            .setMaxItems(limit.count)
            .build()
        l.launch(request)
        val files = results.receive().map { File(it) }  // TODO: scope, check to see if this file is actually public
        val infos = files.map { FileInfo(activity, it) }
        return files.toResult(mimes, limit, infos)
    }

    fun unregister() {
        scope?.cancel()
        scope = null
    }
}