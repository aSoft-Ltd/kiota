package kiota

import kiota.file.FilePickerFactory
import kiota.file.Openable
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime
import kiota.file.response.toSingle
import platform.UIKit.UIViewController

class OSXFilePickerFactory(host: UIViewController) : FilePickerFactory {
    private val documents by lazy { OSXFilePicker(host) }
    private val media by lazy { OSXMediaPicker(host) }

    override fun picker(mimes: Collection<MediaMime>, limit: MemorySize): Openable<SinglePickerResult> = Openable {
        media.show(mimes, PickerLimit(limit, 1)).toSingle()
    }

    override fun picker(mimes: Collection<MediaMime>, limit: PickerLimit): Openable<MultiPickerResult> = Openable {
        media.show(mimes, limit)
    }

    override fun picker(mimes: Iterable<Mime>, limit: MemorySize): Openable<SinglePickerResult> = Openable {
        documents.show(mimes.toList(), PickerLimit(limit, 1)).toSingle()
    }

    override fun picker(mimes: Iterable<Mime>, limit: PickerLimit): Openable<MultiPickerResult> = Openable {
        documents.show(mimes.toList(), limit)
    }
}