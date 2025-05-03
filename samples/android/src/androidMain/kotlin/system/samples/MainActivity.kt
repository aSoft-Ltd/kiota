package system.samples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import system.AndroidLocalFileManager
import system.Sample


class MainActivity : ComponentActivity() {

    private val files by lazy { AndroidLocalFileManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        files.register()
        setContent {
            MaterialTheme {
                Sample(files = files)
            }
        }
    }

    override fun onDestroy() {
        files.unregister()
        super.onDestroy()
    }
}