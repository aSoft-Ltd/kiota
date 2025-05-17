package kiota

import kiota.file.FilePickerFactory
import kiota.internal.OsxFileCreator
import kiota.internal.OsxFileDeleter
import kiota.internal.OsxFileExposer
import kiota.internal.OsxFileInformer
import kiota.internal.OsxFileOpener
import kiota.internal.OsxFileReader
import platform.UIKit.UIViewController

class IosFileManager(host: UIViewController) :
    FileManager,
    FileOpener by OsxFileOpener(host),
    FileReader by OsxFileReader(),
    FileCreator by OsxFileCreator(),
    FileDeleter by OsxFileDeleter(),
    FileInformer by OsxFileInformer(),
    FileExposer by OsxFileExposer(host),
    FilePickerFactory by OSXFilePickerFactory(host)