package com.kpiroom.bubble.util.files

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.bitmap.extractBitmapFrom
import com.kpiroom.bubble.util.bitmap.squareBitmap
import com.kpiroom.bubble.util.constants.DIR_PROFILE_PHOTOS
import com.kpiroom.bubble.util.constants.DIR_PROFILE_WALLPAPERS
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.date.timeStamp
import java.io.File

fun deleteProfilePhoto(photoName: String): Unit = deleteFile(
    DIR_PROFILE_PHOTOS,
    photoName
)

fun deleteProfileWallpaper(wallpaperName: String): Unit = deleteFile(
    DIR_PROFILE_WALLPAPERS,
    wallpaperName
)

fun getCurrentProfileImageName(uuid: String): String = str(
    R.string.template_image_with_timestamp,
    uuid,
    timeStamp
)

fun profilePhotoExists(photoName: String): Boolean = File(
    DIR_PROFILE_PHOTOS,
    photoName
).exists()

fun profileWallpaperExists(wallpaperName: String): Boolean = File(
    DIR_PROFILE_WALLPAPERS,
    wallpaperName
).exists()

fun getProfilePhotoUri(photoName: String): Uri = File(
    DIR_PROFILE_PHOTOS,
    photoName
).toUri()

fun getProfileWallpaperUri(wallpaper: String): Uri = File(
    DIR_PROFILE_WALLPAPERS,
    wallpaper
).toUri()

fun createProfilePhoto(uri: Uri, photoName: String): Uri =
    createCompressedImage(uri, File(DIR_PROFILE_PHOTOS, photoName), square = true)

fun createProfileWallpaper(uri: Uri, wallpaperName: String): Uri =
    createCompressedImage(uri, File(DIR_PROFILE_WALLPAPERS, wallpaperName))

fun createCompressedImage(
    uri: Uri,
    destination: File,
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 50,
    square: Boolean = false
): Uri = destination.apply {
    extractBitmapFrom(uri)?.let {
        if (square) it.squareBitmap else it
    }?.also {
        putCompressedBitmap(it, compressFormat, quality)
    }
}.toUri()