package kiota.sample

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import kiota.IosFileManager
import kiota.Sample

fun Main(host: UIViewController) = ComposeUIViewController {
    Sample(files = IosFileManager()) //
}