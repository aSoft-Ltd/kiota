package system

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import system.internal.LocalFilePath
import system.internal.LocalFileUri
import java.io.File
import java.io.FileInputStream

actual fun ByteArray.toImageBitmap(): ImageBitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size).asImageBitmap()
}

actual suspend fun LocalFileManager.toImageBitmap(file: LocalFile): ImageBitmap = when (file) {
    is LocalFilePath -> toBitmap(file)
    is LocalFileUri -> when (this) {
        is AndroidLocalFileManager -> toBitmap(file)
        else -> throw IllegalArgumentException("Expected AndroidLocalFileManager but found ${this::class.simpleName}")
    }

    else -> throw IllegalArgumentException("Unsupported file type")
}

private fun AndroidLocalFileManager.toBitmap(file: LocalFileUri): ImageBitmap {
    val fd = activity.contentResolver.openFileDescriptor(file.uri, "r")?.fileDescriptor
        ?: throw IllegalArgumentException("File not found")
    return BitmapFactory.decodeFileDescriptor(fd).asImageBitmap()
}

private fun toBitmap(file: LocalFilePath): ImageBitmap {
    val fd = FileInputStream(File(file.path)).fd
    return BitmapFactory.decodeFileDescriptor(fd).asImageBitmap()
}