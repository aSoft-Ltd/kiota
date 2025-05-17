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
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun FileSaver(files: FileManager) {
    val scope = rememberCoroutineScope()
    var message by remember { mutableStateOf<String?>(null) }
    var saved by remember { mutableStateOf<File?>(null) }
    Row {
        Button(
            onClick = {
                scope.launch {
                    message = "Saving file, please wait..."
                    val result = files.create(content = "Saved from samples", name = "sample.txt")
                    message = when (result) {
                        is File -> {
                            saved = result
                            "File saved successfully"
                        }

                        is Failure<*> -> {
                            "Failed to save file: ${result.reasons.joinToString(separator = "\n") { it.message }}"
                        }
                    }
                    delay(3000)
                    message = null
                }
            }
        ) {
            Text("Save Text File")
        }

        Button(
            onClick = {
                scope.launch {
                    message = "Saving file, please wait..."
                    val content = buildString {
                        appendLine("name,age")
                        appendLine("Andy,12")
                        appendLine("Jess,14")
                    }
                    val result = files.create(content = content, name = "sample.csv")
                    message = when (result) {
                        is File -> {
                            saved = result
                            "File saved successfully"
                        }

                        is Failure<*> -> "Failed to save file:\n${result.message}"
                    }
                    delay(3000)
                    message = null
                }
            }
        ) {
            Text("Save CSV File")
        }
    }

    Text(message ?: "")

    val s = saved
    if (s != null) Dialog(
        onDismissRequest = { saved = null }
    ) {
        Column {
            Text("${files.info(s).name()} has been saved")
            Row {
                Button(onClick = {
                    scope.launch {
                        saved = null
                        files.open(s)
                    }
                }) {
                    Text("Open")
                }

                Button(onClick = {
                    scope.launch {
                        saved = null
                        files.export(s)
                    }
                }) {
                    Text("Save")
                }

                if (files.canShare()) Button(onClick = {
                    scope.launch {
                        saved = null
                        files.share(s)
                    }
                }) {
                    Text("Share")
                }
            }
        }
    }
}