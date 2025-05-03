package kiota.file

import kiota.Permission
import kiota.file.mime.All
import kiota.file.mime.Mime

interface PickerPermissionsManager {
    fun check(mimes: List<Mime> = listOf(All)): Permission
    suspend fun request(mimes: List<Mime> = listOf(All)): Permission
}