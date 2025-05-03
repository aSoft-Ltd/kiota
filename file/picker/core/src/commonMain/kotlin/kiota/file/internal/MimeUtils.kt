package kiota.file.internal

import kiota.file.mime.All
import kiota.file.mime.Image
import kiota.file.mime.Mime
import kiota.file.mime.Video

fun List<Mime>.toMediaMimes() = if (contains(All)) {
    listOf(Image, Video)
} else {
    filter { it is Image || it is Video }
}