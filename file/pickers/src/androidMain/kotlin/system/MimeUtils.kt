package system

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VisualMediaType
import system.file.mime.All
import system.file.mime.Audio
import system.file.mime.Image
import system.file.mime.Mime
import system.file.mime.Video

fun List<Mime>.toReadPermissions() = mapNotNull { it.toReadPermission() }.toSet()

fun Mime.toReadPermission(): String? = when (this) {
    All -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        Manifest.permission.READ_EXTERNAL_STORAGE
    } else {
        null
    }

    Audio -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_AUDIO
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        Manifest.permission.READ_EXTERNAL_STORAGE
    } else {
        null
    }

    Video -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_VIDEO
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        Manifest.permission.READ_EXTERNAL_STORAGE
    } else {
        null
    }

    Image -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        Manifest.permission.READ_EXTERNAL_STORAGE
    } else {
        null
    }

    else -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        Manifest.permission.READ_EXTERNAL_STORAGE
    } else {
        null
    }
}

fun List<Mime>.toMediaType(): VisualMediaType {
    val images = count { it is Image || it is All }
    val videos = count { it is Video || it is All }
    return when {
        images > 0 && videos <= 0 -> ActivityResultContracts.PickVisualMedia.ImageOnly
        images <= 0 && videos > 0 -> ActivityResultContracts.PickVisualMedia.VideoOnly
        else -> ActivityResultContracts.PickVisualMedia.ImageAndVideo
    }
}