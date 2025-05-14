@file:OptIn(ExperimentalEncodingApi::class, ExperimentalForeignApi::class)

package kiota.internal

import kiota.File
import kiota.FileDeleter
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import kotlin.io.encoding.ExperimentalEncodingApi

class OsxFileDeleter : FileDeleter {
    override fun delete(file: File): Boolean {
        if (file !is FileUrl) return false
        return NSFileManager.defaultManager.removeItemAtURL(file.url, null)
    }
}