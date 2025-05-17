package kiota

import kiota.file.FilePickerFactory
import kiota.file.Openable
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime
import kiota.file.response.toSingle

class BrowserFilePickerFactory : AbstractPicker(), FilePickerFactory {
    override fun picker(mimes: Collection<MediaMime>, limit: MemorySize): Openable<SinglePickerResult> = Openable {
        show(mimes, PickerLimit(size = limit, count = 1)).toSingle()
    }

    override fun picker(mimes: Collection<MediaMime>, limit: PickerLimit): Openable<MultiPickerResult> = Openable {
        show(mimes, limit)
    }

    override fun picker(mimes: Iterable<Mime>, limit: MemorySize): Openable<SinglePickerResult> = Openable {
        show(mimes.toList(), PickerLimit(size = limit, count = 1)).toSingle()
    }

    override fun picker(mimes: Iterable<Mime>, limit: PickerLimit): Openable<MultiPickerResult> = Openable {
        show(mimes.toList(), limit)
    }
}