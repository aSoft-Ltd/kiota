package kiota

import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusDenied
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHAuthorizationStatusNotDetermined
import platform.Photos.PHAuthorizationStatusRestricted
import platform.Photos.PHPhotoLibrary
import kiota.file.PickerPermissionsManager
import kiota.file.mime.Mime
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OSXMediaPickerPermissionManager : PickerPermissionsManager {
    override fun check(mimes: List<Mime>): Permission = PHPhotoLibrary.authorizationStatus().toPermission()

    override suspend fun request(mimes: List<Mime>): Permission = suspendCoroutine { cont ->
        PHPhotoLibrary.requestAuthorization { status ->
            cont.resume(status.toPermission())
        }
    }

    private fun Long.toPermission() = when (this) {
        PHAuthorizationStatusNotDetermined -> Permission.Unauthorized
        PHAuthorizationStatusAuthorized -> Permission.Granted
        PHAuthorizationStatusLimited -> Permission.Granted
        PHAuthorizationStatusDenied -> Permission.Denied
        PHAuthorizationStatusRestricted -> Permission.Denied
        else -> Permission.Unauthorized
    }
}