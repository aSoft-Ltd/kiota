package system

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual fun ByteArray.toImageBitmap(): ImageBitmap = Image.makeFromEncoded(this).toComposeImageBitmap()

actual suspend fun LocalFileManager.toImageBitmap(file: LocalFile): ImageBitmap = readBytes(file).toImageBitmap()