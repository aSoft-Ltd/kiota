@file:OptIn(ExperimentalEncodingApi::class)

package kiota

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

suspend fun FileManager.toBase64ImageUrl(file: File): String {
    val mime = info(file).mime()
    val bytes = readBytes(file)
    return "data:${mime.text};base64," + Base64.encode(bytes)
}