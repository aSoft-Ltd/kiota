@file:OptIn(ExperimentalComposeUiApi::class, InternalComposeUiApi::class)

package kiota.samples

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kiota.BrowserFileManager
import kiota.Sample
import org.jetbrains.compose.resources.configureWebResources

fun main() {
    configureWebResources {
        resourcePathMapping { "/$it" }
    }

    ComposeViewport("app") {
        MaterialTheme {
            Sample(files = BrowserFileManager())
        }
    }
}