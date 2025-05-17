package kiota

/**
 * # File
 * An abstract representation of a file found on disk
 *
 * ## Android
 *
 * A Local file can be one of the following:
 * 1. A Uri that can be only be resolved with a content resolver
 * 1. A path to a file on disk
 *
 * ## JVM
 * A File is just a path to a file on disk
 *
 * ## Browser (JS & Wasm):
 *
 * A local file is an instance that wraps a File object
 *
 * ## IOS & MacOS
 * A local file is an instance of one of the following
 * 1. A URL that points to a file on disk
 * 1. An NSFileProvideItem that can be resolved with a file provider
 *
 * ## Everywhere else
 * A local file is an instance of a path to a file on disk
 */
interface File :
    FileCreationResult<Nothing>,
    FileExportResult<Nothing>,
    FileOpenResult<Nothing>,
    SinglePickingResult<Nothing>