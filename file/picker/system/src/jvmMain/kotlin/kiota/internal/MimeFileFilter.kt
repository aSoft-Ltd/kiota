package kiota.internal

import kiota.MemorySize
import kiota.bytes
import kiota.file.mime.Mime
import java.io.File
import javax.swing.filechooser.FileFilter

internal class MimeFileFilter(
    private val mimes: Collection<Mime>,
    private val limit: MemorySize,
) : FileFilter() {
    override fun accept(file: File): Boolean {
        if (file.isDirectory) return true
        if (file.length().bytes > limit) return false
        return mimes.any { it.matches(file.extension) }
    }

    override fun getDescription(): String {
        if(mimes.isEmpty()) return "All files"
        if(mimes.size == 1) return mimes.first().name
        return mimes.joinToString(" | ") { it.name }
    }
}