package kiota.file.permissions

import kiota.Permission
import kiota.file.PickerPermissionsManager
import kiota.file.mime.Mime

class VirtualFilePickerPermissionManager(
    private val result: Permission = Permission.Granted
) : PickerPermissionsManager {
    override fun check(mimes: List<Mime>): Permission = result
    override suspend fun request(mimes: List<Mime>): Permission = result
}