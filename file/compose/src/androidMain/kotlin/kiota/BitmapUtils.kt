package kiota

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kiota.internal.FilePath
import kiota.internal.FileUri
import java.io.File
import java.io.FileInputStream

internal fun AndroidFileManager.toBitmap(file: FileUri): ImageBitmap {
    val fd = activity.contentResolver.openFileDescriptor(file.uri, "r")?.fileDescriptor
        ?: throw IllegalArgumentException("File not found")
    return BitmapFactory.decodeFileDescriptor(fd).asImageBitmap()
}

internal fun toBitmap(file: FilePath): ImageBitmap {
    val fd = FileInputStream(File(file.path)).fd
    return BitmapFactory.decodeFileDescriptor(fd).asImageBitmap()
}