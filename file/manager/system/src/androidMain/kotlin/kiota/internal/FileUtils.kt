package kiota.internal

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import kiota.File

internal fun File.toUri(): Uri? = when (this) {
    is FilePath -> Uri.fromFile(java.io.File(path))
    is FileUri -> uri
    else -> null
}

internal fun File.mimeType(context: Context): String? {
    val info = FileInfo(context, this)
    val myMime = MimeTypeMap.getSingleton()
    return myMime.getMimeTypeFromExtension(info.extension())
}