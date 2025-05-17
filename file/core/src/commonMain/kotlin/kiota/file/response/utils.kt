package kiota.file.response

import kiota.Cancelled
import kiota.CountLimitExceeded
import kiota.Denied
import kiota.Failure
import kiota.Files
import kiota.MultiPickerResponse
import kiota.MultiPickerResult
import kiota.PickerFailure
import kiota.SingleFileResponse
import kiota.SinglePickerResult

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

fun MultiPickerResult.toSingle(): SinglePickerResult = when (this) {
    is Cancelled -> this
    is Denied -> this
    is PickerFailure -> this
    is Files -> when (val count = files.size) {
        0 -> Cancelled
        1 -> files.first()
        else -> CountLimitExceeded(count, 1, null)
    }
}