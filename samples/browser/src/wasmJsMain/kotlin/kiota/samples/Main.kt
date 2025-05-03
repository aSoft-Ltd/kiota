@file:OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)

package kiota.samples

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import org.jetbrains.compose.resources.configureWebResources
import kiota.BrowserFileManager
import kiota.Sample

@ExperimentalComposeUiApi
fun main() {
    println("Hello World!")
    configureWebResources {
        resourcePathMapping { "/$it" }
    }

    CanvasBasedWindow(canvasElementId = "app") {
        MaterialTheme {
            Sample(files = BrowserFileManager())
        }
    }
}