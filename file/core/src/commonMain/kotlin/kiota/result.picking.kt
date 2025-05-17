package kiota

import kiota.file.mime.Mime

sealed interface SinglePickingResult

sealed interface MultiPickingResult

sealed interface PickingError : SinglePickingResult, MultiPickingResult {
    val message: String
    var next: PickingError?
    fun all(): List<PickingError> = buildList {
        var curr: PickingError? = this@PickingError
        while (curr != null) {
            add(curr)
            curr = curr.next
        }
    }
}

class CountLimitExceededError(
    val count: Int,
    val limit: Int,
    override var next: PickingError? = null
) : PickingError {
    override val message by lazy {
        "You can only select up to $limit files but you selected $count files"
    }
}

class InvalidMimeTypeError(
    val file: String,
    val mime: Mime,
    val allowed: Collection<Mime>,
    override var next: PickingError? = null,
) : PickingError {
    override val message by lazy {
        "File $file of mime $mime does not match the allowed mime types ($allowed)"
    }
}

class SizeLimitExceededError(
    val file: String,
    val size: MemorySize,
    val limit: MemorySize,
    override var next: PickingError? = null
) : PickingError {
    override val message by lazy {
        "File $file with ${size.toBestSize()} exceeds the limit of $limit"
    }
}