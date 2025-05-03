package kiota.internal

import platform.Foundation.NSItemProvider
import kiota.File

data class FileProvider(val provider: NSItemProvider) : File