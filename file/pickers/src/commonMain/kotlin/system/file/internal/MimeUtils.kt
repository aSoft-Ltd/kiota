package system.file.internal

import system.file.mime.All
import system.file.mime.Image
import system.file.mime.Mime
import system.file.mime.Video

internal fun List<Mime>.toMediaMimes() = if (contains(All)) {
    listOf(Image, Video)
} else {
    filter { it is Image || it is Video }
}