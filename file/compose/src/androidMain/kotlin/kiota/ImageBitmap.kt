package kiota

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kiota.internal.FilePath
import kiota.internal.FileUri

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size).asImageBitmap()
}

actual suspend fun FileReader.toImageBitmap(file: File): ImageBitmap = when (file) {
    is FilePath -> toBitmap(file)
    is FileUri -> when (this) {
        is AndroidFileManager -> toBitmap(file)
        else -> throw IllegalArgumentException("Expected AndroidFileManager but found ${this::class.simpleName}")
    }

    else -> throw IllegalArgumentException("Unsupported file type")
}