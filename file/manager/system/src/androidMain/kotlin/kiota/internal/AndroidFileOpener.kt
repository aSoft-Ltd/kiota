package kiota.internal

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import kiota.Failure
import kiota.FileOpener
import kiota.File
import kiota.SingleFileResponse
import kiota.file.response.ResponseError
import java.io.File as JFile


class AndroidFileOpener(private val context: Context) : FileOpener {

    override suspend fun open(file: File): SingleFileResponse {
        val info = FileInfo(context, file)
        val myMime = MimeTypeMap.getSingleton()
        val newIntent = Intent(Intent.ACTION_VIEW)
        val mimeType = myMime.getMimeTypeFromExtension(info.extension())
        val uri = when (file) {
            is FilePath -> Uri.fromFile(JFile(file.path))
            is FileUri -> file.uri
            else -> return Failure(errors = listOf(ResponseError.UnknownFileType(file)))
        }
        newIntent.setDataAndType(uri, mimeType)
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            context.startActivity(newIntent)
            file
        } catch (e: ActivityNotFoundException) {
            println("No handler for this type of file: $file")
            Failure(errors = listOf(ResponseError.UnknownFileType(file)))
        }
    }

    override suspend fun open(url: String): SingleFileResponse = open(File(url))
}