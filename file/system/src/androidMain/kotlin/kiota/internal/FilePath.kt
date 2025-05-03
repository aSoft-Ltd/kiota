package kiota.internal

import kiota.File

data class FilePath internal constructor(val path: String) : File {
    override fun toString(): String = "File($path)"
}