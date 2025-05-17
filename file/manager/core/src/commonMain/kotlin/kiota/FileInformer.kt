package kiota

/**
 * Get more informed about files
 */
interface FileInformer {
    /**
     * Checks to see if the file exists in the file system
     */
    fun exists(file: File): Boolean

    /**
     * Return general information about the file
     */
    fun info(file: File): FileInfo

    /**
     * Checks whether files can be shared or not
     */
    fun canShare() : Boolean
}