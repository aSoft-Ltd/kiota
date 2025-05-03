package system.file.permissions

import system.Permission
import system.file.PickerPermissionsManager
import system.file.mime.Mime

class VirtualFilePickerPermissionManager(
    private val result: Permission = Permission.Granted
) : PickerPermissionsManager {
    override fun check(mimes: List<Mime>): Permission = result
    override suspend fun request(mimes: List<Mime>): Permission = result
}