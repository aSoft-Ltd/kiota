import kiota.File
import kiota.FileCreator
import kiota.FileManager
import kiota.Multiplier
import kiota.SingleFileResponse
import kiota.VirtualFileManager
import kiota.file.MultiMediaPicker
import kiota.file.SingleFilePicker
import kiota.file.SingleMediaPicker
import kiota.file.mime.Application
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime
import kiota.file.mime.Text
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class FileManagerApiTest {

    interface TestFileCreator {
        suspend fun create(
            content: ByteArray,
            name: String = "file.bin",
            type: Mime = Application.OctetStream
        ): SingleFileResponse

        suspend fun create(
            content: String,
            name: String = "file.txt",
            type: Mime = Text.Plain
        ): SingleFileResponse

        suspend fun expose(file: File) : SingleFileResponse
    }

    fun creator() : TestFileCreator = TODO()

    @Test
    fun should_have_a_good_picker_api() = runTest {
//        val files = creator()
//        val file = files.create(
//            content = "dddd",
//            scope = FileScope.Private
//        )
    }

    data object Single

    data class Multiple(val count: Int)

    interface FilePickerFactory {
        fun picker(mimes: List<MediaMime>, type: Single): SingleMediaPicker
        fun picker(mimes: List<Mime>, type: Single): SingleFilePicker
        fun picker(mimes: List<MediaMime>, type: Multiple): MultiMediaPicker
        fun picker(mimes: List<Mime>, type: Multiple): Multiplier
    }

    @Test
    fun should_have_a_good_file_scoping_api() {
        val files: FileManager = VirtualFileManager()

//        val picker = files.picker(listOf(Image,Video))
    }
}