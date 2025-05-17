package kiota

import kiota.file.FilePickerFactory

/**
 * A quick way to manage files with in the app's private/sandbox.
 */
interface FileManager : FileOpener, FileCreator, FileReader, FileExposer, FileDeleter, FilePickerFactory, FileInformer