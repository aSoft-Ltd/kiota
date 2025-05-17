package kiota

/**
 * An abstraction that can help expose files from the app private/sandbox directory to the system public directory
 */
interface FileExposer {
    /**
     * Prompts the user with a dialog to save the [file] at a directory of their own choosing
     *
     * @return [SingleFileResponse] when the file was successfully saved
     */
    suspend fun export(file: File): SingleFileResponse

    /**
     * Allow the user to share this file with other applications
     */
    suspend fun share(file: File): SingleFileResponse
}