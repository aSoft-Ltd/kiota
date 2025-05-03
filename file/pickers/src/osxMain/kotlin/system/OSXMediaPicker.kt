package system

import kotlinx.coroutines.channels.Channel
import platform.Foundation.NSItemProvider
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import system.file.PickerLimit
import system.file.mime.All
import system.file.mime.Image
import system.file.mime.MediaMime
import system.file.mime.Mime
import system.file.mime.Video
import system.file.toResponse
import system.internal.LocalFileInfoProvider
import system.internal.LocalFileProvider

abstract class OSXMediaPicker {

    private val permission by lazy { OSXMediaPickerPermissionManager() }

    private var host: UIViewController? = null

    private val results = Channel<List<NSItemProvider>>()

    fun initialize(host: UIViewController?) {
        this.host = host
    }

    private val delegate = object : NSObject(), PHPickerViewControllerDelegateProtocol {
        override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
            results.trySend(didFinishPicking.mapNotNull { (it as? PHPickerResult)?.itemProvider })
        }
    }

    private fun Mime.toFilters() = when (this) {
        is Image -> listOf(
            PHPickerFilter.imagesFilter,
            PHPickerFilter.depthEffectPhotosFilter,
            PHPickerFilter.screenshotsFilter,
            PHPickerFilter.panoramasFilter
        )

        is Video -> listOf(
            PHPickerFilter.videosFilter,
            PHPickerFilter.livePhotosFilter,
            PHPickerFilter.cinematicVideosFilter,
            PHPickerFilter.slomoVideosFilter,
            PHPickerFilter.timelapseVideosFilter
        )

        else -> emptyList()
    }

    private fun List<Mime>.toFilter(): PHPickerFilter {
        val filters = if (isEmpty() || contains(All)) {
            Image.toFilters() + Video.toFilters()
        } else buildSet {
            for (m in this@toFilter) {
                addAll(m.toFilters())
            }
        }
        return PHPickerFilter.anyFilterMatchingSubfilters(filters.toList())
    }

    private suspend fun launch(mimes: List<Mime>, limit: PickerLimit): MultiPickerResponse {
        val config = PHPickerConfiguration()
        config.filter = mimes.toFilter()
        config.selectionLimit = limit.count.toLong()

        val chooser = PHPickerViewController(configuration = config)
        chooser.delegate = delegate

        host?.presentViewController(chooser, animated = true, completion = null) ?: throw IllegalStateException(
            "OSXMultiMediaPicker has not been initialized with a non null host view controller"
        )

        val providers = results.receive()

        chooser.dismissViewControllerAnimated(true, completion = null)

        val files = providers.map { LocalFileProvider(it) }
        val infos = files.map { LocalFileInfoProvider(it) }

        return files.toResponse(mimes, limit, infos)
    }

    protected suspend fun show(mimes: List<MediaMime>, limit: PickerLimit): MultiPickerResponse {
        if (permission.check(mimes) == Permission.Granted) return launch(mimes, limit)
        return when (permission.request(mimes)) {
            Permission.Granted -> launch(mimes, limit)
            else -> Denied
        }
    }
}