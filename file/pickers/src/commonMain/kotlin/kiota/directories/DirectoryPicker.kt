package kiota.directories

import koncurrent.Later
import kiota.File
import kiota.file.PickerPermissionsManager

interface DirectoryPicker {
    val permission: PickerPermissionsManager
    fun openDirChooser(): Later<File?>
}