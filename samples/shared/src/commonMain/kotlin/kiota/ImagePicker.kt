package kiota

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Dialog
import kiota.file.PickerLimit
import kiota.file.mime.Image
import kotlinx.coroutines.launch

@Composable
internal fun ImagePicker(
    files: FileManager
) {
    val scope = rememberCoroutineScope()
    Column {
        val picked = remember { mutableStateListOf<File>() }
        val denied = remember { mutableStateOf(false) }
        val errors = remember { mutableStateListOf<PickingError>() }
        Row {
            Button(
                onClick = {
                    scope.launch {
                        when (val result = files.picker(Image).open()) {
                            is Cancelled -> {}
                            is Denied -> denied.value = true
                            is PickingError -> errors += result
                            is File -> picked += result
                        }
                    }
                }
            ) {
                Text("Pick Image")
            }

            Button(
                onClick = {
                    scope.launch {
                        when (val result = files.picker(Image, PickerLimit(count = 4, size = 3.MB)).open()) {
                            is Cancelled -> {}
                            is Denied -> denied.value = true
                            is PickingError -> errors += result
                            is Files -> picked += result
                        }
                    }
                }
            ) {
                Text("Pick Images")
            }

            Button(onClick = { picked.clear() }) {
                Text("Clear All")
            }
        }

        Column {
            for (file in picked) Image(manager = files, file)
        }

        if (errors.isNotEmpty()) Dialog(
            onDismissRequest = { errors.clear() }
        ) {
            Column {
                for (error in errors) Text(error.message)
            }
        }

        if (denied.value) Dialog(
            onDismissRequest = { denied.value = false }
        ) {
            Column {
                Text("Permission denied")
            }
        }
    }
}