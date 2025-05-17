package kiota


data object Cancelled : SinglePickingResult<Nothing>, MultiPickingResult<Nothing>, FileExportResult<Nothing>

data object Denied : SinglePickingResult<Nothing>, MultiPickingResult<Nothing>

data class Failure<out E : Explanation>(val reasons: List<E>) :
    FileCreationResult<E>,
    FileExportResult<E>,
    FileOpenResult<E>,
    SinglePickingResult<E>,
    MultiPickingResult<E>,
    List<E> by reasons {

    constructor(explanation: E) : this(listOf(explanation))

    val message by lazy { reasons.joinToString("\n") { it.message } }

    fun toException() = Exception(message)
}

data class Files(val files: List<File>) : List<File> by files, MultiPickingResult<Nothing>