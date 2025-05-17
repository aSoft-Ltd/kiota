package kiota

import kiota.file.response.ResponseError

sealed interface SingleFileResponse

sealed interface MultiPickerResponse

data object Cancelled : SingleFileResponse, MultiPickerResponse, SinglePickingResult, MultiPickingResult

data object Denied : SingleFileResponse, MultiPickerResponse, SinglePickingResult, MultiPickingResult

data class Failure(val errors: List<ResponseError>) : SingleFileResponse, MultiPickerResponse, List<ResponseError> by errors {
    fun toException() = Exception(errors.joinToString("\n") { it.message })
}

data class Files(val files: List<File>) : MultiPickerResponse, List<File> by files, MultiPickingResult