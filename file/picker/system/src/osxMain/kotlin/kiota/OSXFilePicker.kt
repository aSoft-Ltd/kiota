package kiota

import kiota.file.PickerLimit
import kiota.file.mime.All
import kiota.file.mime.Mime
import kiota.file.toResult
import kiota.internal.FileInfoPath
import kiota.internal.FileUrl
import kotlinx.coroutines.channels.Channel
import platform.Foundation.NSURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.UTTypeItem
import platform.darwin.NSObject

internal class OSXFilePicker(private val host: UIViewController) {

    private val results = Channel<List<NSURL>>()

    private val delegate = object : NSObject(), UIDocumentPickerDelegateProtocol {
        override fun documentPicker(
            controller: UIDocumentPickerViewController,
            didPickDocumentAtURL: NSURL
        ) = documentPicker(controller, listOf(didPickDocumentAtURL))

        override fun documentPicker(controller: UIDocumentPickerViewController, didPickDocumentsAtURLs: List<*>) {
            results.trySend(didPickDocumentsAtURLs.mapNotNull { it as? NSURL })
        }

        override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
            results.trySend(emptyList())
        }
    }

    suspend fun show(mimes: List<Mime>, limit: PickerLimit): MultiPickerResult {
        val types = when {
            mimes.isEmpty() -> listOf(UTTypeItem)
            mimes.contains(All) -> listOf(UTTypeItem)
            else -> mimes.mapNotNull { UTType.typeWithMIMEType(it.text) }
        }

        val picker = UIDocumentPickerViewController(forOpeningContentTypes = types, asCopy = true)

        picker.allowsMultipleSelection = limit.count > 1
        picker.delegate = delegate
        host.presentViewController(picker, animated = true, null)

        val files = results.receive().map { FileUrl(it) }
        picker.dismissViewControllerAnimated(true, null)
        return files.toResult(mimes, limit, files.map { FileInfoPath(it) })
    }
}
