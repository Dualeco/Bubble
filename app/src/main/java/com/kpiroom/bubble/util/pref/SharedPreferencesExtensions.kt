package com.kpiroom.bubble.util.pref

import android.content.SharedPreferences
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.source.pref.UserPrefs
import com.kpiroom.bubble.util.files.getCurrentProfileImageName


fun SharedPreferences.addString(key: String?, value: String?) {
    edit()
        .putString(key, value)
        .apply()
}

fun SharedPreferences.addBoolean(key: String?, value: Boolean) {
    edit()
        .putBoolean(key, value)
        .apply()
}

fun UserPrefs.setFromUser(uuid: String, user: User): Unit = user.let {
    this.uuid = uuid
    username = user.username
    joinedDate = user.joinedDate
    isPhotoSet = user.photoSet
    isWallpaperSet = user.wallpaperSet
    photoName = user.photoName
    wallpaperName = user.wallpaperName
}