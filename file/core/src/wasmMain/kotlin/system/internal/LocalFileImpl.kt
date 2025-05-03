package system.internal

import org.w3c.files.File
import system.LocalFile

data class LocalFileImpl(val wrapped: File): LocalFile