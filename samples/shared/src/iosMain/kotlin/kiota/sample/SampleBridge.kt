package kiota.sample

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import kiota.IosFileManager
import kiota.Sample

class SampleBridge {
    private var controller: UIViewController? = null
    val files = IosFileManager()

    fun make(): UIViewController = ComposeUIViewController {
        Sample(files = files)
    }.also {
        controller = it
        files.initialize(it)
    }
}