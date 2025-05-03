package system

import system.file.response.ResponseError

sealed interface SingleFileResponse

sealed interface MultiPickerResponse

data object Cancelled : SingleFileResponse, MultiPickerResponse

data object Denied : SingleFileResponse, MultiPickerResponse

data class Failure(val errors: List<ResponseError>) : SingleFileResponse, MultiPickerResponse, List<ResponseError> by errors {
    fun toException() = Exception(errors.joinToString("\n") { it.message })
}

data class LocalFiles(val files: List<LocalFile>) : MultiPickerResponse, List<LocalFile> by files