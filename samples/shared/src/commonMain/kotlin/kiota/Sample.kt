package kiota

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

@Composable
fun Sample(
    files: FileManager
) {
    Column {
        FilesPicker(files)
        ImagePicker(files)
        FileReader(files)
        FileSaver(files)
    }
}