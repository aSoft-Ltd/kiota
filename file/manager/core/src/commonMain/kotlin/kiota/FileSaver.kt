package kiota

interface FileSaver {
    /**
     * Prompts the user with a dialog to save the [file] at a directory of their own choosing
     *
     * @return [SingleFileResponse] when the file was successfully saved
     */
    suspend fun saveAs(file: File): SingleFileResponse
}