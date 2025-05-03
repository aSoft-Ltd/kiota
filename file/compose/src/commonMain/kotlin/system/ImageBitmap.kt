package system

import androidx.compose.ui.graphics.ImageBitmap

expect fun ByteArray.toImageBitmap(): ImageBitmap

expect suspend fun LocalFileManager.toImageBitmap(file: LocalFile): ImageBitmap