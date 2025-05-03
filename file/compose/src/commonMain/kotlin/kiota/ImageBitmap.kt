package kiota

import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toImageBitmap(): ImageBitmap

expect suspend fun FileManager.toImageBitmap(file: File): ImageBitmap