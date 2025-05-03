package system.internal

import android.net.Uri
import system.LocalFile

data class LocalFileUri(val uri: Uri) : LocalFile {
    override fun toString(): String = "LocalFile($uri)"
}