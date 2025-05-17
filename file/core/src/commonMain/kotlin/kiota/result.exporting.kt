package kiota

sealed interface FileExportResult<out E : Explanation>

sealed interface FileExportExplanation : Explanation

data class UnknownFile(val file: File) : FileExportExplanation, FileOpenExplanation {
    override val message by lazy { "Unknown file type: ${file::class.simpleName}" }
}