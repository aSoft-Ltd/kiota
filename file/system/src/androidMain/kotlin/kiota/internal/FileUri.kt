package kiota.internal

import android.net.Uri
import kiota.File

data class FileUri(val uri: Uri) : File {
    override fun toString(): String = "File($uri)"
}