package kiota

import platform.Foundation.NSFileManager
import platform.UIKit.UIViewController
import kiota.file.FilePickers
import kiota.file.mime.Mime
import kiota.internal.FileInfoPath
import kiota.internal.FileInfoProvider
import kiota.internal.FileProvider
import kiota.internal.FileUrl
import kiota.internal.OsxFileOpener
import kiota.internal.OsxFileReader
import kiota.internal.OsxFileSaver

class IosFileManager :
    FileManager,
    FileOpener,
    FileReader by OsxFileReader() {
    override val pickers by lazy {
        FilePickers(
            documents = OSXMultiFilePicker(),
            document = OSXSingleFilePicker(),
            medias = OSXMultiMediaPicker(),
            media = OSXSingleMediaPicker()
        )
    }

    private val saver by lazy { OsxFileSaver() }
    private val opener by lazy { OsxFileOpener() }

    fun initialize(host: UIViewController) {
        pickers.documents.initialize(host)
        pickers.document.initialize(host)
        pickers.medias.initialize(host)
        pickers.media.initialize(host)
        saver.initialize(host)
        opener.initialize(host)
    }

    override fun exists(file: File): Boolean = when (file) {
        is FileUrl -> file.url.path?.let { NSFileManager.defaultManager.fileExistsAtPath(it) } ?: false
        is FileProvider -> true
        else -> false
    }

    override fun info(file: File): FileInfo = when (file) {
        is FileUrl -> FileInfoPath(file)
        is FileProvider -> FileInfoProvider(file)
        else -> throw IllegalArgumentException("Unsupported file type on IOS")
    }

    override suspend fun save(content: ByteArray, name: String, type: Mime) = saver.save(content, name, type)
    override suspend fun save(content: String, name: String, type: Mime) = saver.save(content, name, type)

    override suspend fun open(file: File) = opener.open(file)
    override suspend fun open(url: String) = opener.open(url)
}