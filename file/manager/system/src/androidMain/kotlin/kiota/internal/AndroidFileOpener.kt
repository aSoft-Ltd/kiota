package kiota.internal

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import kiota.Failure
import kiota.File
import kiota.FileOpenExplanation
import kiota.FileOpenResult
import kiota.FileOpener
import kiota.FileScope
import kiota.UnknownFile

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

    private fun openPrivate(file: java.io.File, mimeType: String?) = open(
        uri = FileProvider.getUriForFile(context, authority, file),
        mimeType = mimeType
    )

    private fun openPublic(file: File, mimeType: String?) = run {
        return@run open(
            uri = file.toUri() ?: return Failure(UnknownFile(file)),
            mimeType = mimeType
        )
    }

    private fun open(uri: Uri, mimeType: String?): FileOpenResult<FileOpenExplanation> {
        val newIntent = Intent(Intent.ACTION_VIEW)
        newIntent.setDataAndType(uri, mimeType)
        newIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(newIntent)
        return FileUri(uri, FileScope.public)
    }

    override suspend fun open(file: File): FileOpenResult<FileOpenExplanation> {
        val mimeType = file.mimeType(context)
        return when (file) {
            is FilePath -> openPrivate(java.io.File(file.path), mimeType)
            else -> openPublic(file, mimeType)
        }
    }

    override suspend fun open(url: String) = open(File(url, FileScope.public))
}