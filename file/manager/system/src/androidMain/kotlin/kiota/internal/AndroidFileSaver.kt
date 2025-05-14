package kiota.internal

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kiota.Cancelled
import kiota.File
import kiota.FileReader
import kiota.FileSaver
import kiota.SingleFileResponse
import kiota.file.mime.All
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AndroidFileSaver(
    private val activity: ComponentActivity,
    private val reader: FileReader,
) : FileSaver {
    private var scope: CoroutineScope? = null
    private var launcher: ActivityResultLauncher<String>? = null
    private val results by lazy { Channel<Uri?>() }

    fun register() {
        if (launcher != null) return
        scope = CoroutineScope(SupervisorJob())
        launcher = activity.registerForActivityResult(ActivityResultContracts.CreateDocument(All.text)) { uri ->
            scope?.launch { results.send(uri) }
        }
    }

    override suspend fun saveAs(file: File): SingleFileResponse {
        val info = FileInfo(activity, file)
        val l = launcher ?: throw IllegalStateException("Permission manager not registered")
        l.launch(info.name())
        val dst = results.receive() ?: return Cancelled
        withContext(Dispatchers.IO) {
            val dos = activity.contentResolver.openOutputStream(dst) ?: throw IllegalArgumentException(
                "Output stream of selected file could not be opened"
            )
            dos.write(reader.readBytes(file))
            dos.flush()
            dos.close()
        }
        return File(dst)
    }

    fun unregister() {
        launcher?.unregister()
        launcher = null
    }
}