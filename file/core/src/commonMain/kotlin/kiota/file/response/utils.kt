package kiota.file.response

import kiota.Cancelled
import kiota.CountLimitExceededError
import kiota.Denied
import kiota.Failure
import kiota.Files
import kiota.MultiPickerResponse
import kiota.MultiPickingResult
import kiota.PickingError
import kiota.SingleFileResponse
import kiota.SinglePickingResult

fun MultiPickerResponse.toSingle(): SingleFileResponse = when (this) {
    is Cancelled -> this
    is Denied -> this
    is Failure -> this
    is Files -> when (val count = files.size) {
        0 -> Cancelled
        1 -> files.first()
        else -> Failure(errors = listOf(ResponseError.CountLimitExceeded(count, 1)))
    }
}

fun MultiPickingResult.toSingle(): SinglePickingResult = when (this) {
    is Cancelled -> this
    is Denied -> this
    is PickingError -> this
    is Files -> when (val count = files.size) {
        0 -> Cancelled
        1 -> files.first()
        else -> CountLimitExceededError(count, 1, null)
    }
}