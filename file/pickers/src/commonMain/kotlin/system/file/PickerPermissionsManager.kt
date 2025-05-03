package system.file

import system.Permission
import system.file.mime.All
import system.file.mime.Mime

interface PickerPermissionsManager {
    fun check(mimes: List<Mime> = listOf(All)): Permission
    suspend fun request(mimes: List<Mime> = listOf(All)): Permission
}