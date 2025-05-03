package kiota

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kiota.internal.FilePath
import kiota.internal.FileUri
import java.io.File
import java.io.FileInputStream

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size).asImageBitmap()
}

actual suspend fun FileManager.toImageBitmap(file: kiota.File): ImageBitmap = when (file) {
    is FilePath -> toBitmap(file)
    is FileUri -> when (this) {
        is AndroidFileManager -> toBitmap(file)
        else -> throw IllegalArgumentException("Expected AndroidFileManager but found ${this::class.simpleName}")
    }

    else -> throw IllegalArgumentException("Unsupported file type")
}

private fun AndroidFileManager.toBitmap(file: FileUri): ImageBitmap {
    val fd = activity.contentResolver.openFileDescriptor(file.uri, "r")?.fileDescriptor
        ?: throw IllegalArgumentException("File not found")
    return BitmapFactory.decodeFileDescriptor(fd).asImageBitmap()
}

private fun toBitmap(file: FilePath): ImageBitmap {
    val fd = FileInputStream(File(file.path)).fd
    return BitmapFactory.decodeFileDescriptor(fd).asImageBitmap()
}