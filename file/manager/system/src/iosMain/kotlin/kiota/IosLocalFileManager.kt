package kiota

import kiota.file.FilePickers
import kiota.internal.FileInfoPath
import kiota.internal.FileInfoProvider
import kiota.internal.FileProvider
import kiota.internal.FileUrl
import kiota.internal.OsxFileCreator
import kiota.internal.OsxFileDeleter
import kiota.internal.OsxFileOpener
import kiota.internal.OsxFileReader
import kiota.internal.OsxFileExposer
import platform.Foundation.NSFileManager
import platform.UIKit.UIViewController

class IosFileManager :
    FileManager,
    FileOpener,
    FileReader by OsxFileReader(),
    FileCreator by OsxFileCreator(),
    FileDeleter by OsxFileDeleter() {
    override val pickers by lazy {
        FilePickers(
            documents = OSXMultiFilePicker(),
            document = OSXSingleFilePicker(),
            medias = OSXMultiMediaPicker(),
            media = OSXSingleMediaPicker()
        )
    }

    private val saver by lazy { OsxFileExposer() }
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

    override suspend fun export(file: File): SingleFileResponse = saver.export(file)

    override suspend fun open(file: File) = opener.open(file)
    override suspend fun open(url: String) = opener.open(url)
}