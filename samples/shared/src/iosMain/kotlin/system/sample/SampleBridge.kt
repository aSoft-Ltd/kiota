package system.sample

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import system.IosLocalFileManager
import system.Sample

class SampleBridge {
    private var controller: UIViewController? = null
    val files = IosLocalFileManager()

    fun make(): UIViewController = ComposeUIViewController {
        Sample(files = files)
    }.also {
        controller = it
        files.initialize(it)
    }
}