package kiota.sample

import androidx.compose.ui.window.singleWindowApplication
import kiota.JvmFileManager
import kiota.Sample

fun main() {
    singleWindowApplication {
        Sample(files = JvmFileManager())
    }
}