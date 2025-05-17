package kiota

import kiota.file.FilePickerFactory
import kiota.file.Openable
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime
import kiota.file.response.toSingle

class JvmFilePickerFactory : AbstractPicker(), FilePickerFactory {

    override fun picker(mimes: Collection<MediaMime>, limit: MemorySize): Openable<SinglePickingResult> = Openable {
        show(mimes, PickerLimit(size = limit, count = 1)).toSingle()
    }

    override fun picker(mimes: Collection<MediaMime>, limit: PickerLimit): Openable<MultiPickingResult> = Openable {
        show(mimes, limit)
    }

    override fun picker(mimes: Iterable<Mime>, limit: MemorySize): Openable<SinglePickingResult> = Openable {
        show(mimes.toList(), PickerLimit(size = limit, count = 1)).toSingle()
    }

    override fun picker(mimes: Iterable<Mime>, limit: PickerLimit): Openable<MultiPickingResult> = Openable {
        show(mimes.toList(), limit)
    }
}