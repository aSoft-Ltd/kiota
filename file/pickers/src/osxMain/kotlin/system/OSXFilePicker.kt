package system

import kotlinx.coroutines.channels.Channel
import platform.Foundation.NSURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.UTTypeItem
import platform.darwin.NSObject
import system.file.PickerLimit
import system.file.mime.All
import system.file.mime.Mime
import system.file.toResponse
import system.internal.LocalFileInfoPath
import system.internal.LocalFileUrl

abstract class OSXFilePicker {

    private var host: UIViewController? = null
    private val results = Channel<List<NSURL>>()

    fun initialize(host: UIViewController) {
        this.host = host
    }

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

    protected suspend fun show(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse {
        val types = when {
            mimes.isEmpty() -> listOf(UTTypeItem)
            mimes.contains(All) -> listOf(UTTypeItem)
            else -> mimes.mapNotNull { UTType.typeWithMIMEType(it.text) }
        }

        val picker = UIDocumentPickerViewController(forOpeningContentTypes = types, asCopy = true)

        picker.allowsMultipleSelection = limit.count > 1
        picker.delegate = delegate
        host?.presentViewController(picker, animated = true, null) ?: throw IllegalStateException(
            "OSXFilePicker has not been initialized with a non null host view controller"
        )

        val files = results.receive().map { LocalFileUrl(it) }
        picker.dismissViewControllerAnimated(true, null)
        return files.toResponse(mimes, limit, files.map { LocalFileInfoPath(it) })
    }
}
