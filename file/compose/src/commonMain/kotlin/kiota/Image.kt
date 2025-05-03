package kiota

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope.Companion.DefaultFilterQuality
import androidx.compose.ui.layout.ContentScale

@Composable
fun Image(
    manager: FileManager,
    file: File,
    loader: @Composable BoxScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    contentDescription: String? = manager.info(file).name(),
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DefaultFilterQuality
) {
    var bitmap by remember(file) { mutableStateOf<ImageBitmap?>(null) }
    LaunchedEffect(file) {
        bitmap = manager.toImageBitmap(file)
    }
    when (val b = bitmap) {
        null -> Box(modifier, content = loader)
        else -> Image(b, contentDescription, modifier, alignment, contentScale, alpha, colorFilter, filterQuality)
    }
}