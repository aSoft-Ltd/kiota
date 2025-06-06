package kiota

import kiota.file.PickerLimit
import kiota.file.mime.All
import kiota.file.mime.Image
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime
import kiota.file.mime.Video
import kiota.file.toResult
import kiota.internal.FileInfoProvider
import kiota.internal.FileProvider
import kotlinx.coroutines.channels.Channel
import platform.Foundation.NSItemProvider
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

internal class OSXMediaPicker(private val host: UIViewController) {

    private val permission by lazy { OSXMediaPickerPermissionManager() }

    private val results = Channel<List<NSItemProvider>>()

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

    private fun Collection<Mime>.toFilter(): PHPickerFilter {
        val filters = if (isEmpty() || contains(All)) {
            Image.toFilters() + Video.toFilters()
        } else buildSet {
            for (m in this@toFilter) {
                addAll(m.toFilters())
            }
        }
        return PHPickerFilter.anyFilterMatchingSubfilters(filters.toList())
    }

    private suspend fun launch(mimes: Collection<Mime>, limit: PickerLimit): MultiPickingResult<PickingExplanation> {
        val config = PHPickerConfiguration()
        config.filter = mimes.toFilter()
        config.selectionLimit = limit.count.toLong()

        val chooser = PHPickerViewController(configuration = config)
        chooser.delegate = delegate

        host.presentViewController(chooser, animated = true, completion = null)

        val providers = results.receive()

        chooser.dismissViewControllerAnimated(true, completion = null)

        val files = providers.map { FileProvider(it) }
        val infos = files.map { FileInfoProvider(it) }

        return files.toResult(mimes, limit, infos)
    }

    suspend fun show(mimes: Collection<MediaMime>, limit: PickerLimit): MultiPickingResult<PickingExplanation> {
        if (permission.check(mimes) == Permission.Granted) return launch(mimes, limit)
        return when (permission.request(mimes)) {
            Permission.Granted -> launch(mimes, limit)
            else -> Denied
        }
    }
}