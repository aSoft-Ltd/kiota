package kiota

import kiota.file.mime.Mime

sealed interface SinglePickingResult<out E : Explanation>

sealed interface MultiPickingResult<out E : Explanation>

sealed interface PickingExplanation : Explanation

class CountLimitExceeded(
    val count: Int,
    val limit: Int,
) : PickingExplanation {
    override val message by lazy {
        "You can only select up to $limit files but you selected $count files"
    }
}

class InvalidMimeType(
    val file: String,
    val mime: Mime,
    val allowed: Collection<Mime>,
) : PickingExplanation {
    override val message by lazy {
        "File $file of mime $mime does not match the allowed mime types ($allowed)"
    }
}

class SizeLimitExceeded(
    val file: String,
    val size: MemorySize,
    val limit: MemorySize,
) : PickingExplanation {
    override val message by lazy {
        "File $file with ${size.toBestSize()} exceeds the limit of $limit"
    }
}