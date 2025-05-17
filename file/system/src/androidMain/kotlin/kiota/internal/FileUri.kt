package kiota.internal

import android.net.Uri
import kiota.File
import kiota.FileScope

data class FileUri(val uri: Uri, val scope: FileScope) : File {
    override fun toString(): String = "File($uri)"
}