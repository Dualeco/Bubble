package com.kpiroom.bubble.source.pref

import android.preference.PreferenceManager
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.util.pref.addBoolean
import com.kpiroom.bubble.util.pref.addString

class UserPrefs {
    companion object {
        private const val UUID = "uuid"
        private const val USERNAME = "username"
        private const val JOINED_DATE = "joinedOn"
        private const val IS_PHOTO_SET = "isPhotoSet"
        private const val IS_WALLPAPER_SET = "isWallpaperSet"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(app)
    var uuid: String?
        get() = prefs.getString(UUID, null)
        set(value) = prefs.addString(UUID, value)

    var username: String?
        get() = prefs.getString(USERNAME, null)
        set(value) = prefs.addString(USERNAME, value)

    var joinedDate: String?
        get() = prefs.getString(JOINED_DATE, null)
        set(value) = prefs.addString(JOINED_DATE, value)

    var isPhotoSet: Boolean
        get() = prefs.getBoolean(IS_PHOTO_SET, false)
        set(value) = prefs.addBoolean(IS_PHOTO_SET, value)

    var isWallpaperSet: Boolean
        get() = prefs.getBoolean(IS_WALLPAPER_SET, false)
        set(value) = prefs.addBoolean(IS_WALLPAPER_SET, value)

    fun clear() {
        uuid = null
        username = null
        joinedDate = null
        isPhotoSet = false
        isWallpaperSet = false
    }
}