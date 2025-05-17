package kiota

sealed interface FileCreationResult<out E : Explanation>

sealed interface FileCreationExplanation : Explanation

data object OutOfMemory : FileCreationExplanation {
    override val message = "Out of memory"
}

data class InvalidFileName(val name: String) : FileCreationExplanation {
    override val message: String = "`$name` is not a valid file name"
}