package com.kpiroom.bubble.source.pref

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
            prefs.edit()
                .putString(UUID, value)
                .apply()
        }
}