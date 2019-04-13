package com.kpiroom.bubble.source.pref

import android.content.SharedPreferences
import android.net.Uri
import android.preference.PreferenceManager
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.util.pref.addBoolean
import com.kpiroom.bubble.util.pref.addString
import com.kpiroom.bubble.util.pref.clear

class UserPrefs {
    companion object {
        private const val UUID = "id"
        private const val USERNAME = "name"
        private const val JOINED_DATE = "joinedOn"
        private const val PHOTO_NAME = "photoDownloadUri"
        private const val WALLPAPER_NAME = "wallpaperDownloadUri"
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

    var photoDownloadUri: Uri
        get() = Uri.parse(prefs.getString(PHOTO_NAME, ""))
        set(value) = prefs.addString(PHOTO_NAME, value.toString())

    var wallpaperDownloadUri: Uri
        get() = Uri.parse(prefs.getString(WALLPAPER_NAME, ""))
        set(value) = prefs.addString(WALLPAPER_NAME, value.toString())

    fun clear(): Unit = prefs.clear()
}
