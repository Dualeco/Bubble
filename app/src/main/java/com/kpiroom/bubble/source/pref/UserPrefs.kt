package com.kpiroom.bubble.source.pref

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.util.pref.addBoolean
import com.kpiroom.bubble.util.pref.addString
import com.kpiroom.bubble.util.pref.clear

class UserPrefs {
    companion object {
        private const val UUID = "uuid"
        private const val USERNAME = "username"
        private const val JOINED_DATE = "joinedOn"
        private const val IS_PHOTO_SET = "isPhotoSet"
        private const val IS_WALLPAPER_SET = "isWallpaperSet"
        private const val PHOTO_NAME = "photoName"
        private const val WALLPAPER_NAME = "wallpaperName"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(app)
    var uuid: String
        get() = prefs.getString(UUID, "")
        set(value) = prefs.addString(UUID, value)

    var username: String
        get() = prefs.getString(USERNAME, "")
        set(value) = prefs.addString(USERNAME, value)

    var joinedDate: String
        get() = prefs.getString(JOINED_DATE, "")
        set(value) = prefs.addString(JOINED_DATE, value)

    var isPhotoSet: Boolean
        get() = prefs.getBoolean(IS_PHOTO_SET, false)
        set(value) = prefs.addBoolean(IS_PHOTO_SET, value)

    var isWallpaperSet: Boolean
        get() = prefs.getBoolean(IS_WALLPAPER_SET, false)
        set(value) = prefs.addBoolean(IS_WALLPAPER_SET, value)

    var photoName: String
        get() = prefs.getString(PHOTO_NAME, "")
        set(value) = prefs.addString(PHOTO_NAME, value)

    var wallpaperName: String
        get() = prefs.getString(WALLPAPER_NAME, "")
        set(value) = prefs.addString(WALLPAPER_NAME, value)

    fun clear(): Unit = prefs.clear()}
