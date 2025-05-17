package kiota.internal

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kiota.Failure
import kiota.File
import kiota.FileOpener
import kiota.FileScope
import kiota.SingleFileResponse
import kiota.file.response.ResponseError

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
 *     <files-path name="shared" path="./shared" />
 * </paths>
 */
class AndroidFileOpener(
    private val context: Context,
    private val authority: String = "${context.applicationContext.packageName}.fileprovider"
) : FileOpener {

    private fun openPrivate(file: java.io.File, mimeType: String?): SingleFileResponse {
        val uri = FileProvider.getUriForFile(context, authority, file)
        return open(uri, mimeType)
    }

    private fun openPublic(file: File, mimeType: String?): SingleFileResponse {
        val uri = file.toUri() ?: return Failure(
            errors = listOf(ResponseError.UnknownFileType(file))
        )
        return open(uri, mimeType)
    }

    private fun open(uri: Uri, mimeType: String?): SingleFileResponse {
        val newIntent = Intent(Intent.ACTION_VIEW)
        newIntent.setDataAndType(uri, mimeType)
        newIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(newIntent)
        return FileUri(uri, FileScope.public)
    }

    override suspend fun open(file: File): SingleFileResponse {
        val mimeType = file.mimeType(context)
        return when (file) {
            is FilePath -> openPrivate(java.io.File(file.path), mimeType)
            else -> openPublic(file, mimeType)
        }
    }

    override suspend fun open(url: String): SingleFileResponse = open(File(url, FileScope.public))
}