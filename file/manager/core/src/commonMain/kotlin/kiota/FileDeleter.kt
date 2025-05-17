package kiota

/**
 * Grants ability to delete files from the app private/sandbox directory
 */
interface FileDeleter {
    /**
     * Deletes the file from the app private/sandbox directory
     *
     * @return true if the file was deleted and false if the file was not deleted
     */
    fun delete(file: File): Boolean
}