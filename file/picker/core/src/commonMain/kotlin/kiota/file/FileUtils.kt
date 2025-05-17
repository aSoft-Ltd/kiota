package kiota.file

import kiota.Cancelled
import kiota.CountLimitExceeded
import kiota.Failure
import kiota.File
import kiota.FileInfo
import kiota.Files
import kiota.InvalidMimeType
import kiota.MemorySize
import kiota.MultiPickingResult
import kiota.PickingExplanation
import kiota.SizeLimitExceeded
import kiota.file.mime.Mime
import kiota.file.response.ResponseError

//private suspend fun FileInfo.fits(mimes: List<Mime>, limit: MemorySize): List<ResponseError> = buildList {
//    val mime = Mime.from(extension())
//    val name = name()
//    if (mimes.none { it.matches(mime) }) add(ResponseError.InvalidMimeType(name, mime, mimes))
//    val size = size()
//    if (size > limit) add(ResponseError.SizeLimitExceeded(name, size, limit))
//}

//suspend fun List<File>.toResponse(
//    mimes: List<Mime>,
//    limit: PickerLimit,
//    infos: List<FileInfo>
//): MultiPickerResponse {
//    if (isEmpty()) return Cancelled
//    val errors = buildList {
//        if (size > limit.count) {
//            add(ResponseError.CountLimitExceeded(size, limit.count))
//        }
//        for (file in infos) {
//            addAll(file.fits(mimes, limit.size))
//        }
//    }
//    if (errors.isNotEmpty()) return Failure(errors)
//    return Files(this)
//}

private suspend fun FileInfo.satisfies(mimes: Collection<Mime>, limit: MemorySize): List<PickingExplanation> = buildList {
    val mime = Mime.from(extension())
    val name = name()
    if (mimes.none { it.matches(mime) }) add(InvalidMimeType(name, mime, mimes))
    val size = size()
    if (size > limit) add(SizeLimitExceeded(name, size, limit))
}

suspend fun List<File>.toResult(
    mimes: Collection<Mime>,
    limit: PickerLimit,
    infos: Collection<FileInfo>
): MultiPickingResult<PickingExplanation> {
    if (isEmpty()) return Cancelled
    val errors = buildList {
        if (size > limit.count) {
            add(CountLimitExceeded(size, limit.count))
        }
        for (file in infos) {
            addAll(file.satisfies(mimes, limit.size))
        }
    }
    if (errors.isNotEmpty()) return Failure(errors)
    return Files(this)
}