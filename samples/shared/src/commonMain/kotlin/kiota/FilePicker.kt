package kiota

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import kiota.file.PickerLimit
import kiota.file.mime.Mime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round

@Composable
internal fun FilesPicker(
    files: FileManager
) {
    val scope = rememberCoroutineScope()
    Column {
        val picked = remember { mutableStateListOf<File>() }
        val denied = remember { mutableStateOf(false) }
        val errors = remember { mutableStateListOf<PickerFailure>() }
        Row {
            Button(
                onClick = {
                    scope.launch {
                        when (val result = files.picker(limit = PickerLimit(size = 10.MB, count = 2)).open()) {
                            Cancelled -> {}
                            Denied -> denied.value = true
                            is PickerFailure -> errors += result.all()
                            is Files -> picked += result
                        }
                    }
                }
            ) {
                Text("Pick Files")
            }

            Button(
                onClick = {
                    scope.launch {
                        when (val result = files.picker().open()) {
                            is Cancelled -> {}
                            is Denied -> denied.value = true
                            is PickerFailure -> errors += result
                            is File -> picked += result
                        }
                    }
                }
            ) {
                Text("Pick File")
            }
        }

        Column {
            for (file in picked) PickedFile(files, files.info(file))
        }

        if (errors.isNotEmpty()) Dialog(
            onDismissRequest = {
                errors.clear()
            }
        ) {
            Column(Modifier.fillMaxSize(0.9f)) {
                for ((idx, error) in errors.withIndex()) {
                    Text("${idx + 1}/${errors.size}: ${error.message}")
                }
            }
        }

        if (denied.value) Dialog(
            onDismissRequest = {
                denied.value = false
            }
        ) {
            Column {
                Text("Permission denied")
            }
        }
    }
}

@Composable
internal fun PickedFile(
    files: FileManager,
    file: FileInfo
) {
    var size by remember { mutableStateOf(MemorySize.Zero) }
    var message by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    LaunchedEffect(file) {
        val s = file.size().toBestSize()
        size = s.copy(value = round(s.value * 10) / 10)
    }
    Column {
        Text(
            "File: ${file.name()}, Size: $size"
        )
        Row {
            Button(onClick = {
                scope.launch {
                    message = "Saving file, please wait..."
                    val result = files.create(
                        content = files.readBytes(file.file),
                        name = file.name(),
                        type = Mime.from(extension = file.extension())
                    )
                    message = when (result) {
                        is File -> "File saved successfully"
                        is Failure -> "Failed to save file: ${result.errors.joinToString(", ") { it.message ?: "" }}"
                        else -> "File save cancelled"
                    }
                    delay(3000)
                    message = null
                }
            }) { Text("Save Privately") }

            Button(onClick = {
                scope.launch { files.open(file.file) }
            }) { Text("Open") }

            Button(onClick = {
                scope.launch { files.export(file.file) }
            }) { Text("Save As") }
        }
        Text(message ?: "")
    }
}