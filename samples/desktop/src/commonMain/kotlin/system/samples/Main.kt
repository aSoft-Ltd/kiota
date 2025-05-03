package system.samples

import androidx.compose.ui.window.singleWindowApplication
import system.JvmLocalFileManager
import system.Sample

fun main() {
    singleWindowApplication {
        Sample(files = JvmLocalFileManager())
    }
}