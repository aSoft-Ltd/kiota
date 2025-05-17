package kiota

import androidx.activity.ComponentActivity
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime
import kiota.internal.AndroidFileCreator
import kiota.internal.AndroidFileDeleter
import kiota.internal.AndroidFileExposer
import kiota.internal.AndroidFileOpener
import kiota.internal.AndroidFileReader
import kiota.internal.FileInfo
import kiota.internal.FilePath
import kiota.internal.FileUri
import kiota.internal.Folders
import java.io.File as JFile

/**
 * Requires to have a FileProvider defined in AndroidManifest.xml
 * <application>
 *     ...
 *     <provider
 *         android:name="androidx.core.content.FileProvider"
 *         android:authorities="${applicationId}.fileprovider"
 *         android:exported="false"
 *         android:grantUriPermissions="true">
 *         <meta-data
 *             android:name="android.support.FILE_PROVIDER_PATHS"
 *             android:resource="@xml/file_paths" />
 *     </provider>
 *     ...
 * </application>
 * and
 * xml/file_paths.xml with contents
 * <paths>
 *     <files-path name="tmp" path="./tmp" />
 * </paths>
 */
class AndroidFileManager(
    val activity: ComponentActivity,
    private val authority: String = "${activity.applicationContext.packageName}.fileprovider",
    private val folders: Folders = Folders()
) :
    FileManager,
    FileCreator,
    FileDeleter by AndroidFileDeleter(),
    FileReader by AndroidFileReader(activity),
    FileOpener by AndroidFileOpener(activity, authority) {
    private val factory by lazy { AndroidFilePickerFactory(activity) }

    private val creator by lazy { AndroidFileCreator(activity) }

    private val saver by lazy { AndroidFileExposer(activity, this, folders, authority) }

    fun register() {
        factory.register()
        saver.register()
    }

    override fun exists(file: File): Boolean = when (file) {
        is FilePath -> JFile(file.path).exists()
        is FileUri -> when (file.uri.scheme) {
            "content" -> {
                val cursor = activity.contentResolver.query(file.uri, null, null, null, null)
                val res = cursor != null
                cursor?.close()
                res
            }

            "file" -> JFile(file.uri.path ?: "").exists()
            else -> false
        }

        else -> false
    }

    override fun info(file: File): FileInfo = FileInfo(activity, file)
    override fun canShare(): Boolean = true

    override suspend fun create(content: ByteArray, name: String, type: Mime) = creator.create(content, name, type)
    override suspend fun create(content: String, name: String, type: Mime) = creator.create(content, name, type)
    override suspend fun export(file: File): SingleFileResponse = saver.export(file)
    override suspend fun share(file: File): SingleFileResponse = saver.share(file)

    override fun picker(mimes: Collection<MediaMime>, limit: MemorySize) = factory.picker(mimes, limit)
    override fun picker(mimes: Collection<MediaMime>, limit: PickerLimit) = factory.picker(mimes, limit)
    override fun picker(mimes: Iterable<Mime>, limit: MemorySize) = factory.picker(mimes, limit)
    override fun picker(mimes: Iterable<Mime>, limit: PickerLimit) = factory.picker(mimes, limit)

    fun unregister() {
        factory.unregister()
        saver.unregister()
    }
}