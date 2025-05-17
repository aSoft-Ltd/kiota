@file:OptIn(ExperimentalForeignApi::class)

package kiota.internal

import kiota.Cancelled
import kiota.Denied
import kiota.File
import kiota.FileExposer
import kiota.SingleFileResponse
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.channels.Channel
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIDevice
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIUserInterfaceIdiomPad
import platform.UIKit.UIViewController
import platform.UIKit.popoverPresentationController
import platform.darwin.NSObject

internal class OsxFileExposer(private val host: UIViewController) : FileExposer {

    private val results = Channel<NSURL?>()


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

        host.presentViewController(picker, animated = true, null)

        val url = results.receive() ?: return Cancelled
        picker.dismissViewControllerAnimated(true, null)
        return FileUrl(url)
    }

    override suspend fun share(file: File): SingleFileResponse {
        if (file !is FileUrl) return Cancelled
        val url = file.url
        val activity = UIActivityViewController(activityItems = listOf(url), applicationActivities = null)

        if (UIDevice.currentDevice.userInterfaceIdiom == UIUserInterfaceIdiomPad) {
            activity.popoverPresentationController?.sourceView = host.view
            host.view.bounds.useContents {
                activity.popoverPresentationController?.sourceRect = CGRectMake(origin.x, origin.y, 0.0, 0.0)
            }
            activity.popoverPresentationController?.permittedArrowDirections = 0uL
        }
        host.presentViewController(activity, animated = true, completion = null)
        return file // TODO: Handle file sharing results
    }
}