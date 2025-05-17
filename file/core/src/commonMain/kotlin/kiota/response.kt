package kiota

import kiota.file.mime.Mime
import kiota.file.response.ResponseError

sealed interface SingleFileResponse

sealed interface MultiPickerResponse

data object Cancelled : SingleFileResponse, MultiPickerResponse, SinglePickerResult, MultiPickerResult

data object Denied : SingleFileResponse, MultiPickerResponse, SinglePickerResult, MultiPickerResult

data class Failure(val errors: List<ResponseError>) : SingleFileResponse, MultiPickerResponse, List<ResponseError> by errors {
    fun toException() = Exception(errors.joinToString("\n") { it.message })
}

data class Files(val files: List<File>) : MultiPickerResponse, List<File> by files, MultiPickerResult

// File Picker
sealed interface SinglePickerResult

sealed interface MultiPickerResult

sealed interface PickerFailure : SinglePickerResult, MultiPickerResult {
    val message: String
    var next: PickerFailure?
    fun all(): List<PickerFailure> = buildList {
        var curr: PickerFailure? = this@PickerFailure
        while (curr != null) {
            add(curr)
            curr = curr.next
        }
    }
}

class CountLimitExceeded(
    val count: Int,
    val limit: Int,
    override var next: PickerFailure? = null
) : PickerFailure {
    override val message by lazy {
        "You can only select up to $limit files but you selected $count files"
    }
}

class InvalidMimeType(
    val file: String,
    val mime: Mime,
    val allowed: Collection<Mime>,
    override var next: PickerFailure? = null,
) : PickerFailure {
    override val message by lazy {
        "File $file of mime $mime does not match the allowed mime types ($allowed)"
    }
}

class SizeLimitExceeded(
    val file: String,
    val size: MemorySize,
    val limit: MemorySize,
    override var next: PickerFailure? = null
) : PickerFailure {
    override val message by lazy {
        "File $file with ${size.toBestSize()} exceeds the limit of $limit"
    }
}