package kiota.internal

import kiota.File

data class FileImpl(val path: String) : File {
    override fun toString(): String = "File($path)"
}