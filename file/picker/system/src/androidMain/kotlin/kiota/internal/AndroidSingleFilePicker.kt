package kiota.internal

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kiota.Cancelled
import kiota.FileScope
import kiota.MemorySize
import kiota.PickingExplanation
import kiota.SinglePickingResult
import kiota.file.PickerLimit
import kiota.file.mime.All
import kiota.file.mime.Mime
import kiota.file.response.toSingle
import kiota.file.toResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal class AndroidSingleFilePicker(private val activity: ComponentActivity) {
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

    suspend fun open(
        mimes: Collection<Mime>,
        limit: MemorySize,
    ): SinglePickingResult<PickingExplanation> {
        if (mimes.isEmpty()) return open(listOf(All), limit)
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        l.launch(mimes.map { it.text }.toTypedArray())
        val uri = results.receive() ?: return Cancelled
        val files = listOf(FileUri(uri, FileScope.public))   // TODO: scope, check to see if this file is actually public
        val infos = files.map { FileInfoUri(activity, it) }
        return files.toResult(mimes, PickerLimit(limit, 1), infos).toSingle()
    }

    fun unregister() {
        scope?.cancel()
        scope = null
    }
}