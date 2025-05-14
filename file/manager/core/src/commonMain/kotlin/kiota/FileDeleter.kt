package kiota

interface FileDeleter {
    /**
     * Deletes the file from private/app directory
     *
     * @return true if the file was deleted and false if the file was not deleted
     */
    fun delete(file: File): Boolean
}