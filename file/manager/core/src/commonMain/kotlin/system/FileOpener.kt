package system

/**
 * Can ask the system to open a particular file
 *
 * In the browser, this will open a new tab with the file
 * In the native platform, this will open the file with the default application
 */
interface FileOpener {
    suspend fun open(file: LocalFile): SingleFileResponse

    /**
     * Open a file with the given [url]
     */
    suspend fun open(url: String): SingleFileResponse
}