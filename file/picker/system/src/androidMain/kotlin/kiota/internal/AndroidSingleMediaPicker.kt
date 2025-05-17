package kiota.internal

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import kiota.Cancelled
import kiota.MemorySize
import kiota.PickingExplanation
import kiota.SinglePickingResult
import kiota.file.PickerLimit
import kiota.file.mime.Image
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime
import kiota.file.mime.Video
import kiota.file.response.toSingle
import kiota.file.toResult
import kiota.toMediaType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal class AndroidSingleMediaPicker(private val activity: ComponentActivity) {
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
        mimes: Collection<Mime>,
        limit: MemorySize,
    ): SinglePickingResult<PickingExplanation> {
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        val request = PickVisualMediaRequest.Builder()
            .setMediaType(mimes.toMediaType())
            .build()
        l.launch(request)
        val uri = results.receive() ?: return Cancelled
        val files = listOf(File(uri))
        val infos = files.map { FileInfo(activity, it) }
        return files.toResult(mimes, PickerLimit(limit, 1), infos).toSingle()
    }

    suspend fun open(
        mimes: Collection<MediaMime>,
        limit: MemorySize,
    ): SinglePickingResult<PickingExplanation> {
        if (mimes.isEmpty()) return open(listOf(Image, Video), limit)
        return show(mimes, limit)
    }

    fun unregister() {
        scope?.cancel()
        scope = null
    }
}