package system.file.response

import system.Cancelled
import system.Denied
import system.Failure
import system.LocalFiles
import system.MultiPickerResponse
import system.SingleFileResponse

fun MultiPickerResponse.toSingle(): SingleFileResponse = when (this) {
    is Cancelled -> this
    is Denied -> this
    is Failure -> this
    is LocalFiles -> when (val count = files.size) {
        0 -> Cancelled
        1 -> files.first()
        else -> Failure(errors = listOf(ResponseError.CountLimitExceeded(count, 1)))
    }
}