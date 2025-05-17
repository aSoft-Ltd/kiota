@file:OptIn(ExperimentalEncodingApi::class, ExperimentalForeignApi::class, BetaInteropApi::class)

package kiota.internal

import kiota.Failure
import kiota.FileCreationExplanation
import kiota.InvalidFileName
import kiota.FileCreationResult
import kiota.FileCreator
import kiota.OutOfMemory
import kiota.file.mime.Mime
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDataBase64DecodingIgnoreUnknownCharacters
import platform.Foundation.NSFileManager
import platform.Foundation.create
import platform.Foundation.temporaryDirectory
import platform.Foundation.writeToURL
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class OsxFileCreator : FileCreator {

    override suspend fun create(content: ByteArray, name: String, type: Mime): FileCreationResult<FileCreationExplanation> {
        val tmp = NSFileManager.defaultManager.temporaryDirectory
        val src = tmp.URLByAppendingPathComponent(name) ?: return Failure(InvalidFileName(name))
        val data = NSData.create(
            base64EncodedString = Base64.encode(content),
            options = NSDataBase64DecodingIgnoreUnknownCharacters
        ) ?: return Failure(OutOfMemory)
        withContext(Dispatchers.IO) {
            data.writeToURL(url = src, atomically = false)
        }
        return FileUrl(src)
    }

    override suspend fun create(content: String, name: String, type: Mime) = create(
        content = content.encodeToByteArray(),
        name = name,
        type = type
    )
}