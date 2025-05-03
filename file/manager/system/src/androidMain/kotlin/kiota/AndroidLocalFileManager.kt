package kiota

import androidx.activity.ComponentActivity
import kiota.file.FilePickers
import kiota.file.mime.Mime
import kiota.internal.AndroidFileOpener
import kiota.internal.AndroidFileReader
import kiota.internal.AndroidFileCreator
import kiota.internal.FileInfo
import kiota.internal.FilePath
import kiota.internal.FileUri
import java.io.File

class AndroidFileManager(val activity: ComponentActivity) :
    FileManager,
    FileCreator,
    FileReader by AndroidFileReader(activity),
    FileOpener by AndroidFileOpener(activity) {
    override val pickers by lazy {
        FilePickers(
            documents = AndroidMultiFilePicker(activity),
            document = AndroidSingleFilePicker(activity),
            medias = AndroidMultiMediaPicker(activity),
            media = AndroidSingleMediaPicker(activity),
        )
    }

    private val saver by lazy { AndroidFileCreator(activity) }

    fun register() {
        pickers.documents.register()
        pickers.document.register()
        pickers.medias.register()
        pickers.media.register()
        saver.register()
    }

    override fun exists(file: kiota.File): Boolean = when (file) {
        is FilePath -> File(file.path).exists()
        is FileUri -> when (file.uri.scheme) {
            "content" -> {
                val cursor = activity.contentResolver.query(file.uri, null, null, null, null)
                val res = cursor != null
                cursor?.close()
                res
            }

            "file" -> File(file.uri.path ?: "").exists()
            else -> false
        }

        else -> false
    }

    override fun info(file: kiota.File): FileInfo = FileInfo(activity, file)

    override suspend fun create(content: ByteArray, name: String, type: Mime) = saver.create(content, name, type)
    override suspend fun create(content: String, name: String, type: Mime) = saver.create(content, name, type)

    fun unregister() {
        pickers.documents.unregister()
        pickers.document.unregister()
        pickers.medias.unregister()
        pickers.media.unregister()
        saver.unregister()
    }
}
