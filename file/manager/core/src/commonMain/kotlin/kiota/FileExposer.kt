package kiota

/**
 * An abstraction that can help expose files from the app private/sandbox directory to the system public directory
 */
interface FileExposer {
    /**
     * Prompts the user with a dialog to save the [file] at a directory of their own choosing
     *
     * @return [FileExportResult]
     */
    suspend fun export(file: File): FileExportResult

    /**
     * Allows the user to share this file
     * @return [FileExportResult]
     */
    suspend fun share(file: File): FileExportResult
}