@file:OptIn(ExperimentalEncodingApi::class)

package kiota.internal

import kiota.Cancelled
import kiota.Denied
import kiota.File
import kiota.FileExposer
import kiota.SingleFileResponse
import kotlinx.coroutines.channels.Channel
import platform.Foundation.NSURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import kotlin.io.encoding.ExperimentalEncodingApi

class OsxFileExposer : FileExposer {

    private var host: UIViewController? = null
    private val results = Channel<NSURL?>()

    fun initialize(host: UIViewController) {
        this.host = host
    }

    private val delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {
        override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentAtURL: NSURL) {
            results.trySend(didPickDocumentAtURL)
        }

        override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentsAtURLs: List<*>) {
            results.trySend(didPickDocumentsAtURLs.firstNotNullOfOrNull { it as? NSURL })
        }

        override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
            results.trySend(null)
        }
    }

    override suspend fun export(file: File): SingleFileResponse {
        if (file !is FileUrl) return Denied
        val picker = UIDocumentPickerViewController(forExportingURLs = listOf(file.url), asCopy = true)

        picker.allowsMultipleSelection = false
        picker.delegate = delegate

        host?.presentViewController(picker, animated = true, null) ?: throw IllegalStateException(
            "OSXFileSaver has not been initialized with a non null host view controller"
        )
        val url = results.receive() ?: return Cancelled
        picker.dismissViewControllerAnimated(true, null)
        return FileUrl(url)
    }
}