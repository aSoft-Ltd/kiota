package kiota

sealed interface FileOpenResult<out E : Explanation>

sealed interface FileOpenExplanation : Explanation

data class OpenerNotFound(val file: File) : FileExportExplanation {
    override val message by lazy { "Application that can open this file has not been found" }
}