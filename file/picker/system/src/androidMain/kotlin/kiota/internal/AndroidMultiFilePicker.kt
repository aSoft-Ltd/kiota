package kiota.internal

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kiota.FileScope
import kiota.MultiPickingResult
import kiota.PickingExplanation
import kiota.file.PickerLimit
import kiota.file.mime.All
import kiota.file.mime.Mime
import kiota.file.toResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

internal class AndroidMultiFilePicker(private val activity: ComponentActivity) {
    private var scope: CoroutineScope? = null
    private var launcher: ActivityResultLauncher<Array<String>>? = null
    private val results by lazy { Channel<List<Uri>>() }

    fun register() {
        if (launcher != null) return
        scope = CoroutineScope(SupervisorJob())
        launcher = activity.registerForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris ->
            scope?.launch { results.send(uris) }
        }
    }

    suspend fun open(
        mimes: List<Mime>,
        limit: PickerLimit,
    ): MultiPickingResult<PickingExplanation> {
        if (mimes.isEmpty()) return open(listOf(All), limit)
        val l = launcher ?: throw IllegalStateException("AndroidFileChooser has not been registered")
        l.launch(mimes.map { it.text }.toTypedArray())
        val files = results.receive().map { File(it, FileScope.public) } // TODO: scope, check to see if this file is actually public
        val info = files.map { FileInfo(activity, it) }
        return files.toResult(mimes, limit, info)
    }

    fun unregister() {
        scope?.cancel()
        scope = null
    }
}