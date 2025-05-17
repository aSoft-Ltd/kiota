package kiota.file.response

import kiota.Cancelled
import kiota.CountLimitExceeded
import kiota.Denied
import kiota.Failure
import kiota.Files
import kiota.MultiPickingResult
import kiota.PickingExplanation
import kiota.SinglePickingResult

fun MultiPickingResult<*>.toSingle(): SinglePickingResult<PickingExplanation> = when (this) {
    is Cancelled -> this
    is Denied -> this
    is Failure<*> -> Failure(reasons.filterIsInstance<PickingExplanation>())
    is Files -> when (val count = files.size) {
        0 -> Cancelled
        1 -> files.first()
        else -> Failure(CountLimitExceeded(count, 1))
    }
}