package system.internal

import system.LocalFile

data class LocalFilePath internal constructor(val path: String) : LocalFile {
    override fun toString(): String = "LocalFile($path)"
}