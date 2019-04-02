package com.kpiroom.bubble.util.pref

import android.content.SharedPreferences
import android.net.Uri
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.source.pref.UserPrefs

fun SharedPreferences.addString(key: String, value: String): Unit = edit()
    .putString(key, value)
    .apply()

fun SharedPreferences.addBoolean(key: String, value: Boolean): Unit = edit()
    .putBoolean(key, value)
    .apply()

fun UserPrefs.setFromUser(uuid: String, user: User): Unit = user.let {
    this.uuid = uuid
    username = user.username
    joinedDate = user.joinedDate
    photoDownloadUri = Uri.parse(user.photoDownloadUri)
    wallpaperDownloadUri = Uri.parse(user.wallpaperDownloadUri)
}

fun SharedPreferences.clear(): Unit = edit()
    .clear()
    .apply()