@file:OptIn(BetaInteropApi::class, ExperimentalEncodingApi::class)

package kiota.internal

import kotlinx.cinterop.BetaInteropApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSDataBase64DecodingIgnoreUnknownCharacters
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerMode
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTTypeFolder
import platform.darwin.NSObject
import kiota.Cancelled
import kiota.Failure
import kiota.FileCreator
import kiota.SingleFileResponse
import kiota.file.mime.Mime
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class OsxFileCreator : FileCreator {

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


    private suspend fun directory(): NSURL? = withContext(Dispatchers.Main) {
        val picker = UIDocumentPickerViewController(documentTypes = listOf(UTTypeFolder.identifier), inMode = UIDocumentPickerMode.UIDocumentPickerModeOpen)

        picker.allowsMultipleSelection = false
        picker.delegate = delegate

        host?.presentViewController(picker, animated = true, null) ?: throw IllegalStateException(
            "OSXFilePicker has not been initialized with a non null host view controller"
        )

        val directory = results.receive()
        picker.dismissViewControllerAnimated(true, null)
        directory
    }

    override suspend fun create(content: ByteArray, name: String, type: Mime): SingleFileResponse {
        val dir = directory() ?: return Cancelled

        val url = dir.URLByAppendingPathComponent(name) ?: return Failure(errors = emptyList()).also {
            println("Failed to create file URL")
        }

        return withContext(Dispatchers.IO) {
            val data = NSData.create(
                base64EncodedString = Base64.encode(content),
                options = NSDataBase64DecodingIgnoreUnknownCharacters
            ) ?: return@withContext Failure(errors = emptyList()).also {
                println("Failed to create NSData from content")
            }

            data.writeToURL(url = url, atomically = true)
            FileUrl(url)
        }
    }

    override suspend fun create(content: String, name: String, type: Mime) = create(
        content = content.encodeToByteArray(),
        name = name,
        type = type
    )
}