package kiota

import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

suspend fun FileReader.toBitmapPainter(
    file: File,
    srcOffset: IntOffset = IntOffset.Zero,
    srcSize: IntSize? = null,
    filterQuality: FilterQuality = FilterQuality.Low
): BitmapPainter {
    val image = toImageBitmap(file)
    return BitmapPainter(
        image = toImageBitmap(file),
        srcOffset = srcOffset,
        srcSize = srcSize ?: IntSize(image.width, image.height),
        filterQuality = filterQuality
    )
}