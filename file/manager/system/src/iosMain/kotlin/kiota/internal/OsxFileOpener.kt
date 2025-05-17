package kiota.internal

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSURL
import platform.UIKit.UIDocumentInteractionController
import platform.UIKit.UIDocumentInteractionControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTTypeFileURL
import platform.darwin.NSObject
import kiota.FileOpener
import kiota.File
import kiota.SingleFileResponse
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class OsxFileOpener(private val host: UIViewController) : FileOpener {
    private val delegate = object : NSObject(), UIDocumentInteractionControllerDelegateProtocol {
        override fun documentInteractionControllerViewControllerForPreview(controller: UIDocumentInteractionController) = host
    }

    private fun open(url: NSURL): SingleFileResponse {
        val controller = UIDocumentInteractionController()
        controller.URL = url
        controller.delegate = delegate
        controller.presentPreviewAnimated(animated = true)
        return FileUrl(url)
    }

    override suspend fun open(file: File) = open(file.toUrl())

    private suspend fun File.toUrl(): NSURL = when (this) {
        is FileUrl -> url
        is FileProvider -> toUrl()
        else -> throw IllegalArgumentException("Unsupported file type on IOS")
    }

    private suspend fun FileProvider.toUrl(): NSURL = suspendCancellableCoroutine { cont ->
        provider.loadItemForTypeIdentifier(UTTypeFileURL.identifier, null) { data, error ->
            if (error != null) {
                cont.resumeWithException(RuntimeException(error.localizedDescription))
            } else if (data != null) {
                val url = data as? NSURL
                if (url != null) {
                    cont.resume(url)
                } else {
                    cont.resumeWithException(RuntimeException("Data is not null, but it is not a NSURL either"))
                }
            } else {
                cont.resumeWithException(IllegalStateException("Data and error are both null"))
            }
        }
    }

    override suspend fun open(url: String): SingleFileResponse = open(NSURL.fileURLWithPath(url))
}