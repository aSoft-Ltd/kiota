package kiota.internal

import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import kiota.Cancelled
import kiota.Failure
import kiota.File
import kiota.FileExportExplanation
import kiota.FileExportResult
import kiota.FileExposer
import kiota.FileManager
import kiota.FileScope
import kiota.OpenerNotFound
import kiota.file.mime.All
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.File as JFile

class AndroidFileExposer(
    private val activity: ComponentActivity,
    private val manager: FileManager,
    private val folders: Folders,
    private val authority: String
) : FileExposer {
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

    override suspend fun export(file: File): FileExportResult<FileExportExplanation> {
        val info = FileInfo(activity, file)
        val l = launcher ?: throw IllegalStateException("Permission manager not registered")
        l.launch(info.name())
        val dst = results.receive() ?: return Cancelled
        withContext(Dispatchers.IO) {
            val dos = activity.contentResolver.openOutputStream(dst) ?: throw IllegalArgumentException(
                "Output stream of selected file could not be opened"
            )
            file.writeTo(dos)
        }
        return File(dst, FileScope.public)
    }

    private suspend fun File.writeTo(os: OutputStream) = withContext(Dispatchers.IO) {
        os.write(manager.readBytes(this@writeTo))
        os.flush()
        os.close()
    }

    override suspend fun share(file: File): FileExportResult<FileExportExplanation> {
        val tmp = JFile(activity.filesDir, folders.shared)
        if (!tmp.exists()) tmp.mkdirs()

        val i = manager.info(file)
        val f = JFile(tmp, i.name()).also { it.createNewFile() }

        withContext(Dispatchers.IO) {
            file.writeTo(FileOutputStream(f))
        }

        val uri = FileProvider.getUriForFile(activity, authority, f)

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = file.mimeType(activity)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission to the receiving app
        }

        // Optionally, add a subject to the share intent
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing a file")

        // Create a chooser intent to allow the user to select an app
        val chooserIntent = Intent.createChooser(shareIntent, "Share file via")

        return try {
            activity.startActivity(chooserIntent)
            file
        } catch (e: android.content.ActivityNotFoundException) {
            Failure(OpenerNotFound(file))
        }
    }

    fun unregister() {
        launcher?.unregister()
        launcher = null
    }
}