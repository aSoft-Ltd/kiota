package system

import androidx.activity.ComponentActivity
import system.file.FilePickers
import system.file.mime.Mime
import system.internal.AndroidFileOpener
import system.internal.AndroidFileReader
import system.internal.AndroidFileSaver
import system.internal.FileInfo
import system.internal.LocalFilePath
import system.internal.LocalFileUri
import java.io.File

class AndroidLocalFileManager(val activity: ComponentActivity) :
    LocalFileManager,
    FileSaver,
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

    private val saver by lazy { AndroidFileSaver(activity) }

    fun register() {
        pickers.documents.register()
        pickers.document.register()
        pickers.medias.register()
        pickers.media.register()
        saver.register()
    }

    override fun exists(file: LocalFile): Boolean = when (file) {
        is LocalFilePath -> File(file.path).exists()
        is LocalFileUri -> when (file.uri.scheme) {
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

    override fun info(file: LocalFile): FileInfo = FileInfo(activity, file)

    override suspend fun save(content: ByteArray, name: String, type: Mime) = saver.save(content, name, type)
    override suspend fun save(content: String, name: String, type: Mime) = saver.save(content, name, type)

    fun unregister() {
        pickers.documents.unregister()
        pickers.document.unregister()
        pickers.medias.unregister()
        pickers.media.unregister()
        saver.unregister()
    }
}
