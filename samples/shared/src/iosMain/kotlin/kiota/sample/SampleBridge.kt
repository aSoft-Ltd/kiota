package kiota.sample

import androidx.compose.ui.interop.LocalUIViewController
import androidx.compose.ui.window.ComposeUIViewController
import kiota.IosFileManager
import kiota.Sample
import platform.UIKit.UIViewController

class SampleBridge {
    fun make(): UIViewController = ComposeUIViewController {
        Sample(files = IosFileManager(LocalUIViewController.current))
    }
}