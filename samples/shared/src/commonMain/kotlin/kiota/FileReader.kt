package kiota

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

@Composable
internal fun FileReader(files: FileManager) {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf("") }
    Column {
        Row {
            Button(
                onClick = {
                    scope.launch {
                        when (val file = files.pickers.document.open()) {
                            is Cancelled -> {}
                            is Denied -> {}
                            is Failure -> {}
                            is File -> {
                                text = files.readText(file)
                            }
                        }
                    }
                }
            ) {
                Text("Read File")
            }

            Button(onClick = { text = "" }) {
                Text("Clear Text")
            }
        }
        Text(text = text)
    }
}