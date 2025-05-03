package system.file.response

import system.LocalFile
import system.MemorySize
import system.file.mime.Mime

sealed interface ResponseError {
    val message: String

    class InvalidMimeType(
        val file: String,
        val mime: Mime,
        val allowed: List<Mime>
    ) : ResponseError {
        override val message by lazy {
            "File $file of mime $mime does not match the allowed mime types ($allowed)"
        }
    }

    class FileIsDirectory(
        val file: String
    ) : ResponseError {
        override val message by lazy { "File $file is a directory" }
    }

    class SizeLimitExceeded(
        val file: String,
        val size: MemorySize,
        val limit: MemorySize
    ) : ResponseError {
        override val message by lazy {
            "File $file with ${size.toBestSize()} exceeds the limit of $limit"
        }
    }

    class CountLimitExceeded(
        val count: Int,
        val limit: Int
    ) : ResponseError {
        override val message by lazy {
            "You can only select up to $limit files but you selected $count files"
        }
    }

    class UnknownFileType(
        val file: LocalFile
    ) : ResponseError {
        override val message by lazy { "File $file is of type ${file::class.simpleName}" }
    }

    fun toException() = Exception(message)
}