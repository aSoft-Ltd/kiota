package kiota.internal

import android.content.Context
import android.provider.OpenableColumns
import kiota.FileInfo
import kiota.MemorySize
import kiota.MemoryUnit
import kiota.Multiplier

class FileInfoUri(
    context: Context,
    override val file: FileUri
) : FileInfo {
    val resolver = context.contentResolver

    private fun fullname(): String {
        val cursor = resolver.query(file.uri, null, null, null, null) ?: return ""
        if (!cursor.moveToFirst()) {
            cursor.close()
            return ""
        }
        val column = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
        val full = cursor.getString(column) ?: ""
        cursor.close()
        return full
    }

    override fun name(extension: Boolean): String {
        val full = fullname()
        if (extension) return full
        val ext = extension()
        if (ext == "") return full
        return full.substringBefore(".$ext")
    }

    override fun extension(): String = fullname().split(".").lastOrNull() ?: ""

    override suspend fun size(): MemorySize {
        val cursor = resolver.query(file.uri, null, null, null, null) ?: return MemorySize.Zero
        if (!cursor.moveToFirst()) {
            cursor.close()
            return MemorySize.Zero
        }
        val column = cursor.getColumnIndex(OpenableColumns.SIZE)
        val size = cursor.getLong(column)
        cursor.close()
        return MemorySize(
            value = size.toDouble(),
            unit = MemoryUnit.Bytes,
            multiplier = Multiplier.Unit
        )
    }
}