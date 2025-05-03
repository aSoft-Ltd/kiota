package kiota

import kiota.file.mime.All
import kiota.file.mime.Mime

interface SaverPermissionsManager {
    fun check(mime: Mime = All): Permission
    suspend fun request(mime: Mime = All): Permission
}