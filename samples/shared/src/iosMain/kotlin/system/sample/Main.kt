package system.sample

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import system.IosLocalFileManager
import system.Sample

fun Main(host: UIViewController) = ComposeUIViewController {
    Sample(files = IosLocalFileManager()) //
}