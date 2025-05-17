package kiota

import kiota.file.mime.Mime
import kiota.internal.ByteArrayFile
import kiota.internal.TextFile

class VirtualFileCreator(private val files: MutableMap<String, File>) : FileCreator {
    override suspend fun create(content: ByteArray, name: String, type: Mime): FileCreationResult<FileCreationExplanation> {
        val file = ByteArrayFile(content, name, type)
        val path = "file://test/file_${file.hashCode()}/$name"
        files[path] = file
        return file
    }

    override suspend fun create(content: String, name: String, type: Mime): FileCreationResult<FileCreationExplanation> {
        val file = TextFile(content, name, type)
        val path = "file://test/file_${file.hashCode()}/$name"
        files[path] = file
        return file
    }
}