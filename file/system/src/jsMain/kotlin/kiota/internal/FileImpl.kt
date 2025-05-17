package kiota.internal

import kiota.File
import kiota.FileScope
import org.w3c.files.File as BrowserFile

data class FileImpl(val wrapped: BrowserFile,val scope: FileScope) : File