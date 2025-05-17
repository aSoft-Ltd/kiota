package kiota.file

import kiota.GB
import kiota.MemorySize
import kiota.PickingExplanation
import kiota.MultiPickingResult
import kiota.SinglePickingResult
import kiota.file.mime.All
import kiota.file.mime.MediaMime
import kiota.file.mime.Mime

interface FilePickerFactory {
    /**
     * Returns a picker that can pick a single media file
     *
     * @param mimes filters the type of media file that can be picked
     * @param limit describes the size limit of the file that can be picked
     */
    fun picker(mimes: Collection<MediaMime>, limit: MemorySize = 2.GB): Openable<SinglePickingResult<PickingExplanation>>

    /**
     * Returns a picker that can pick a single media file
     *
     * @param mime filters the type of media file that can be picked
     * @param limit describes the size limit of the file that can be picked
     */
    fun picker(mime: MediaMime, limit: MemorySize = PickerLimit.Default.size): Openable<SinglePickingResult<PickingExplanation>> = picker(listOf(mime), limit)

    /**
     * Returns a picker that can pick a multiple media files
     *
     * @param mimes filters the types of media files that can be picked
     * @param limit describes the size limit per file and the number of media files that can be picked
     */
    fun picker(mimes: Collection<MediaMime>, limit: PickerLimit): Openable<MultiPickingResult<PickingExplanation>>

    /**
     * Returns a picker that can pick a multiple media files
     *
     * @param mime filters the types of media files that can be picked
     * @param limit describes the size limit per file and the number of media files that can be picked
     */
    fun picker(mime: MediaMime, limit: PickerLimit): Openable<MultiPickingResult<PickingExplanation>> = picker(listOf(mime), limit)

    /**
     * Returns a file picker that can pick a single file
     *
     * @param mimes filters the type of the file that can be picked
     * @param limit describes the size limit of the file that can be picked
     */
    fun picker(mimes: Iterable<Mime> = listOf(All), limit: MemorySize = PickerLimit.Default.size): Openable<SinglePickingResult<PickingExplanation>>

    /**
     * Returns a file picker that can pick a single file
     *
     * @param mime filters the type of the file that can be picked
     * @param limit describes the size limit of the file that can be picked
     */
    fun picker(mime: Mime, limit: MemorySize = PickerLimit.Default.size): Openable<SinglePickingResult<PickingExplanation>> = picker(listOf(mime), limit)

    /**
     * Returns a picker that can pick a multiple files
     *
     * @param mimes filters the types of files that can be picked
     * @param limit describes the size limit per file and the number of files that can be picked
     */
    fun picker(mimes: Iterable<Mime> = listOf(All), limit: PickerLimit): Openable<MultiPickingResult<PickingExplanation>>

    /**
     * Returns a picker that can pick a multiple files
     *
     * @param mime filters the types of files that can be picked
     * @param limit describes the size limit per file and the number of files that can be picked
     */
    fun picker(mime: Mime, limit: PickerLimit): Openable<MultiPickingResult<PickingExplanation>> = picker(listOf(mime), limit)
}