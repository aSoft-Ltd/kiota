package system

import platform.Foundation.NSFileManager
import platform.UIKit.UIViewController
import system.file.FilePickers
import system.file.mime.Mime
import system.internal.LocalFileInfoPath
import system.internal.LocalFileInfoProvider
import system.internal.LocalFileProvider
import system.internal.LocalFileUrl
import system.internal.OsxFileOpener
import system.internal.OsxFileReader
import system.internal.OsxFileSaver

class IosLocalFileManager :
    LocalFileManager,
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

    override fun exists(file: LocalFile): Boolean = when (file) {
        is LocalFileUrl -> file.url.path?.let { NSFileManager.defaultManager.fileExistsAtPath(it) } ?: false
        is LocalFileProvider -> true
        else -> false
    }

    override fun info(file: LocalFile): FileInfo = when (file) {
        is LocalFileUrl -> LocalFileInfoPath(file)
        is LocalFileProvider -> LocalFileInfoProvider(file)
        else -> throw IllegalArgumentException("Unsupported file type on IOS")
    }

    override suspend fun save(content: ByteArray, name: String, type: Mime) = saver.save(content, name, type)
    override suspend fun save(content: String, name: String, type: Mime) = saver.save(content, name, type)

    override suspend fun open(file: LocalFile) = opener.open(file)
    override suspend fun open(url: String) = opener.open(url)
}