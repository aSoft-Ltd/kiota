package system

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import system.file.PickerLimit
import system.file.SingleFilePicker
import system.file.mime.All
import system.file.mime.Mime
import system.file.response.toSingle
import system.file.toResponse
import system.internal.FileInfoUri
import system.internal.LocalFileUri

class AndroidSingleFilePicker(private val activity: ComponentActivity) : SingleFilePicker {
    private var scope: CoroutineScope? = null
    private var launcher: ActivityResultLauncher<Array<String>>? = null
    private val results by lazy { Channel<Uri?>() }

    fun register() {
        if (launcher != null) return
        scope = CoroutineScope(SupervisorJob())
        launcher = activity.registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            scope?.launch { results.send(uri) }
        }
    }

    override suspend fun open(
        mimes: List<Mime>,
        limit: MemorySize,
    ): SingleFileResponse {
        if (mimes.isEmpty()) return open(listOf(All), limit)
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        l.launch(mimes.map { it.text }.toTypedArray())
        val files = listOf(results.receive()?.let { LocalFileUri(it) } ?: return Cancelled)
        val infos = files.map { FileInfoUri(activity, it) }
        return files.toResponse(mimes, PickerLimit(limit, 1), infos).toSingle()
    }

    fun unregister() {
        scope?.cancel()
        scope = null
    }
}