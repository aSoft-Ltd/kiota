package system.internal

import platform.Foundation.NSItemProvider
import system.LocalFile

data class LocalFileProvider(val provider: NSItemProvider) : LocalFile