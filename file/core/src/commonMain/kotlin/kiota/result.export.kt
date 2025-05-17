package kiota

sealed interface FileExportResult<out T>

data object Success : FileExportResult<Nothing>

data object Postponed : FileExportResult<Nothing>

data class Rejected<T>(val reason: T) : FileExportResult<T>

sealed interface FileExportExplanation

data class UnknownFile(val file: File) : FileExportExplanation