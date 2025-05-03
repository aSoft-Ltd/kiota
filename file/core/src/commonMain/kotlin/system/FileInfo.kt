package system

interface FileInfo {
    val file: LocalFile

    /**
     * @return The name of the file, with or without the extension.
     *
     * @param [extension] If false, the extension will be excluded in the name.
     * @param [extension] If true, the extension will be included in the name.
     */
    fun name(extension: Boolean = true): String

    /**
     * @return the extension of the file, without the dot.
     */
    fun extension(): String

    /**
     * Returns the size of the file.
     *
     * This operation may be expensive, as it may require reading the file's metadata.
     * or reading entire files
     */
    suspend fun size(): MemorySize
}