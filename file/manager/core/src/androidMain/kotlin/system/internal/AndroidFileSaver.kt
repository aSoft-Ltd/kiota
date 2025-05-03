package system.internal

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import system.Cancelled
import system.FileSaver
import system.SingleFileResponse
import system.file.mime.All
import system.file.mime.Mime
import java.io.OutputStream

class AndroidFileSaver(private val activity: ComponentActivity) : FileSaver {
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

    override suspend fun save(content: ByteArray, name: String, type: Mime) = save(name) {
        write(content)
    }

    override suspend fun save(content: String, name: String, type: Mime) = save(name) {
        val w = writer()
        w.append(content)
        w.flush()
        w.close()
    }

    private suspend fun save(
        name: String,
        block: OutputStream.() -> Unit
    ): SingleFileResponse {
        val l = launcher ?: throw IllegalStateException("Permission manager not registered")
        l.launch(name)
        val uri = results.receive() ?: return Cancelled

        withContext(Dispatchers.IO) {
            val os = activity.contentResolver.openOutputStream(uri) ?: throw IllegalArgumentException(
                "Output stream of selected file could not be opened"
            )
            os.block()
            os.flush()
            os.close()
        }
        return LocalFile(uri)
    }

    fun unregister() {
        launcher?.unregister()
        launcher = null
    }
}