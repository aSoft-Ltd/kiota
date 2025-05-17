package kiota.internal

import kiota.JvmMultiFilePicker
import kiota.JvmMultiMediaPicker
import kiota.JvmSingleFilePicker
import kiota.JvmSingleMediaPicker
import kiota.MemorySize
import kiota.MultiPickerResult
import kiota.SinglePickerResult
import kiota.file.FilePickerFactory
import kiota.file.FilePickers
import kiota.file.Openable
import kiota.file.PickerLimit
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime

class JvmFilePickerFactory : FilePickerFactory {
    private val pickers by lazy {
        FilePickers(
            documents = JvmMultiFilePicker(),
            document = JvmSingleFilePicker(),
            medias = JvmMultiMediaPicker(),
            media = JvmSingleMediaPicker(),
        )
    }

    override fun picker(mimes: Collection<MediaMime>, limit: MemorySize): Openable<SinglePickerResult> {
        TODO("Not yet implemented")
    }

    override fun picker(mimes: Collection<MediaMime>, limit: PickerLimit): Openable<MultiPickerResult> {
        TODO("Not yet implemented")
    }

    override fun picker(mimes: Iterable<Mime>, limit: MemorySize): Openable<SinglePickerResult> {
        TODO("Not yet implemented")
    }

    override fun picker(mimes: Iterable<Mime>, limit: PickerLimit): Openable<MultiPickerResult> {
        TODO("Not yet implemented")
    }

}