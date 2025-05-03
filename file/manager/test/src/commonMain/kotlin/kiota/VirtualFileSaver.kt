package kiota

import kiota.file.mime.Mime
import kiota.internal.ByteArrayFile
import kiota.internal.TextFile

class VirtualFileSaver(private val files: MutableMap<String, File>) : FileSaver {
    override suspend fun save(content: ByteArray, name: String, type: Mime): SingleFileResponse {
        val file = ByteArrayFile(content, name, type)
        val path = "file://test/file_${file.hashCode()}/$name"
        files[path] = file
        return file
    }

    override suspend fun save(content: String, name: String, type: Mime): SingleFileResponse {
        val file = TextFile(content, name, type)
        val path = "file://test/file_${file.hashCode()}/$name"
        files[path] = file
        return file
    }
}