package com.kpiroom.bubble.source.pref

import android.net.Uri
import android.preference.PreferenceManager
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.util.pref.addString

class UserPrefs {
    companion object {
        private const val UUID = "uuid"
        private const val USERNAME = "username"
        private const val PROFILE_PHOTO_URI = "profilePhoto"
        private const val PROFILE_WALLPAPER_URI = "profileWallpaper"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(app)
    var uuid: String?
        get() = prefs.getString(UUID, null)
        set(value) = prefs.addString(UUID, value)

    var username: String?
        get() = prefs.getString(USERNAME, null)
        set(value) = prefs.addString(USERNAME, value)

    var profilePhotoUri: Uri?
        get() = prefs.getString(PROFILE_PHOTO_URI, null)?.let {
            Uri.parse(it)
        }
        set(value) = prefs.addString(PROFILE_PHOTO_URI, value.toString())

    var profileWallpaperUri: Uri?
        get() = prefs.getString(PROFILE_WALLPAPER_URI, null)?.let {
            Uri.parse(it)
        }
        set(value) = prefs.addString(PROFILE_WALLPAPER_URI, value.toString())
}