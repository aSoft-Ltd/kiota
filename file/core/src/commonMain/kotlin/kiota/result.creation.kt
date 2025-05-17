package kiota

sealed interface FileCreationResult

sealed interface FileCreationError : FileCreationResult {
    val message: String
}

data object OutOfMemoryError : FileCreationError {
    override val message = "Out of memory"
}

data class InvalidFileNameError(val name: String) : FileCreationError {
    override val message: String = "`$name` is not a valid file name"
}