package com.kpiroom.bubble.source.pref

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.kpiroom.bubble.os.BubbleApp.Companion.app

class UserPrefs {
    companion object {
        const val UUID = "uuid"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(app)
    var uuid: String?
        get() = prefs.getString(UUID, null)
        set(value) {
            prefs.addString(UUID, value)
        }
}

fun SharedPreferences.addString(key: String?, value: String?) {

    this.edit()
        .putString(key, value)
        .apply()
}