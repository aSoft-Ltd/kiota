package system.directories

import koncurrent.Later
import system.LocalFile
import system.file.PickerPermissionsManager

interface DirectoryPicker {
    val permission: PickerPermissionsManager
    fun openDirChooser(): Later<LocalFile?>
}