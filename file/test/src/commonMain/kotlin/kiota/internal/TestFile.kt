package kiota.internal

import kiota.File
import kiota.file.mime.Mime

interface TestFile : File {
    val name: String
    val mime: Mime
}