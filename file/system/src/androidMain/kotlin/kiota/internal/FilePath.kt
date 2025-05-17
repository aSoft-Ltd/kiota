package kiota.internal

import kiota.File
import kiota.FileScope

data class FilePath internal constructor(val path: String, val scope: FileScope) : File {
    override fun toString(): String = "File($path)"
}