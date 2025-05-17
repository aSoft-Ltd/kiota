package kiota

import androidx.activity.ComponentActivity
import kiota.file.FilePickerFactory
import kiota.file.FilePickers
import kiota.file.Openable
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime

class AndroidFilePickerFactory(private val activity: ComponentActivity) : FilePickerFactory {
    private val pickers by lazy {
        FilePickers(
            documents = AndroidMultiFilePicker(activity),
            document = AndroidSingleFilePicker(activity),
            medias = AndroidMultiMediaPicker(activity),
            media = AndroidSingleMediaPicker(activity),
        )
    }

    fun register() {
        pickers.documents.register()
        pickers.document.register()
        pickers.medias.register()
        pickers.media.register()
    }

    override fun picker(mimes: Collection<MediaMime>, limit: MemorySize): Openable<SinglePickerResult> = Openable {
        pickers.media.open(mimes, limit)
    }

    override fun picker(mimes: Collection<MediaMime>, limit: PickerLimit): Openable<MultiPickerResult> = Openable {
        pickers.medias.open(mimes, limit)
    }

    override fun picker(mimes: Iterable<Mime>, limit: MemorySize): Openable<SinglePickerResult> = Openable {
        pickers.document.open(mimes.toList(), limit)
    }

    override fun picker(mimes: Iterable<Mime>, limit: PickerLimit): Openable<MultiPickerResult> = Openable {
        pickers.documents.open(mimes.toList(), limit)
    }

    fun unregister() {
        pickers.documents.unregister()
        pickers.document.unregister()
        pickers.medias.unregister()
        pickers.media.unregister()
    }
}