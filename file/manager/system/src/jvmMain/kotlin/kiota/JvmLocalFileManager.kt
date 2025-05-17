package kiota

import kiota.file.FilePickerFactory
import kiota.internal.JvmFileCreator
import kiota.internal.JvmFileDeleter
import kiota.internal.JvmFileExposer
import kiota.internal.JvmFileInformer
import kiota.internal.JvmFileOpener
import kiota.internal.JvmFileReader

class JvmFileManager(private val sandbox: java.io.File = java.io.File("./tmp")) :
    FileManager,
    FileReader by JvmFileReader(),
    FileCreator by JvmFileCreator(sandbox),
    FileDeleter by JvmFileDeleter(),
    FileExposer by JvmFileExposer(),
    FileOpener by JvmFileOpener(),
    FileInformer by JvmFileInformer(),
    FilePickerFactory by JvmFilePickerFactory()