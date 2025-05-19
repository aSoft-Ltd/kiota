package kiota

import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toImageBitmap(): ImageBitmap

expect suspend fun FileReader.toImageBitmap(file: File): ImageBitmap