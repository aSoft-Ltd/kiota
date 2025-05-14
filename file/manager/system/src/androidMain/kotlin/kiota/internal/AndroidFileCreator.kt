package kiota.internal

import android.content.ContentValues
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import kiota.Cancelled
import kiota.FileCreator
import kiota.SingleFileResponse
import kiota.file.mime.All
import kiota.file.mime.Mime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.OutputStream

class AndroidFileCreator(private val activity: ComponentActivity) : FileCreator {
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

    override suspend fun create(content: ByteArray, name: String, type: Mime) = save(name) {
        write(content)
    }

    override suspend fun create(content: String, name: String, type: Mime) = save(name) {
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
            val uos = activity.contentResolver.openOutputStream(uri) ?: throw IllegalArgumentException(
                "Output stream of selected file could not be opened"
            )
            uos.block()
            uos.flush()
            uos.close()
        }
        return File(uri)
    }

    fun unregister() {
        launcher?.unregister()
        launcher = null
    }
}