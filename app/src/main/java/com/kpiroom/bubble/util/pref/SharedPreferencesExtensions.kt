package com.kpiroom.bubble.util.pref

import android.content.SharedPreferences
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.source.pref.UserPrefs


fun SharedPreferences.addString(key: String, value: String?): Unit = edit()
    .putString(key, value)
    .apply()

fun SharedPreferences.addBoolean(key: String, value: Boolean): Unit = edit()
    .putBoolean(key, value)
    .apply()

fun SharedPreferences.clear(): Unit = edit()
    .clear()
    .apply()

fun UserPrefs.setFromUser(uuid: String, user: User?) {
    user?.let {
        this.uuid = uuid
        username = user.username
        joinedDate = user.joinedDate
        isPhotoSet = user.isPhotoSet
        isWallpaperSet = user.isWallpaperSet
    }
}