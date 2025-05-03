package system.file

import system.Cancelled
import system.Failure
import system.FileInfo
import system.LocalFiles
import system.LocalFile
import system.MemorySize
import system.MultiPickerResponse
import system.file.mime.Mime
import system.file.response.ResponseError

private suspend fun FileInfo.fits(mimes: List<Mime>, limit: MemorySize): List<ResponseError> = buildList {
    val mime = Mime.from(extension())
    val name = name()
    if (mimes.none { it.matches(mime) }) add(ResponseError.InvalidMimeType(name, mime, mimes))
    val size = size()
    if (size > limit) add(ResponseError.SizeLimitExceeded(name, size, limit))
}

internal suspend fun List<LocalFile>.toResponse(
    mimes: List<Mime>,
    limit: PickerLimit,
    infos: List<FileInfo>
): MultiPickerResponse {
    if (isEmpty()) return Cancelled
    val errors = buildList {
        if (size > limit.count) {
            add(ResponseError.CountLimitExceeded(size, limit.count))
        }
        for (file in infos) {
            addAll(file.fits(mimes, limit.size))
        }
    }
    if (errors.isNotEmpty()) return Failure(errors)
    return LocalFiles(this)
}