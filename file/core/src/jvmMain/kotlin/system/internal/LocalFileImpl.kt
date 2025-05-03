package system.internal

import system.LocalFile

data class LocalFileImpl(val path: String) : LocalFile {
    override fun toString(): String = "LocalFile($path)"
}