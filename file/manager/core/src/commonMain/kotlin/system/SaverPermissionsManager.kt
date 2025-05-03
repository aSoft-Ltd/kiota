package system

import system.file.mime.All
import system.file.mime.Mime

interface SaverPermissionsManager {
    fun check(mime: Mime = All): Permission
    suspend fun request(mime: Mime = All): Permission
}