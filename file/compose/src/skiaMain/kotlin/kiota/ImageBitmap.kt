package kiota

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image

actual fun ByteArray.toImageBitmap(): ImageBitmap = Image.makeFromEncoded(this).toComposeImageBitmap()

actual suspend fun FileReader.toImageBitmap(file: File): ImageBitmap = readBytes(file).toImageBitmap()