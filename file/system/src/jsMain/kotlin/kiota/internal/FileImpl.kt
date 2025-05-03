package kiota.internal

import kiota.File
import org.w3c.files.File as BrowserFile

data class FileImpl(val wrapped: BrowserFile) : File