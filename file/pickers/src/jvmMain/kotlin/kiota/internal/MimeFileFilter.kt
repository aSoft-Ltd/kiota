package kiota.internal

import kiota.MemorySize
import kiota.bytes
import kiota.file.mime.Mime
import java.io.File
import javax.swing.filechooser.FileFilter

internal class MimeFileFilter(
    private val mimes: List<Mime>,
    private val limit: MemorySize,
) : FileFilter() {
    override fun accept(file: File): Boolean {
        if (mimes.none { it.matches(file.extension) }) return false
        return file.length().bytes <= limit
    }

    override fun getDescription(): String = mimes.map { it.name }.firstOrNull() ?: "All files"
}