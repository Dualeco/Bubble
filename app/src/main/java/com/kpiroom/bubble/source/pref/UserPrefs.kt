package com.kpiroom.bubble.source.pref

import android.preference.PreferenceManager
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.util.pref.addString

class UserPrefs {
    companion object {
        private const val UUID = "uuid"
    }

    private val prefs = PreferenceManager.getDefaultSharedPreferences(app)
    var uuid: String?
        get() = prefs.getString(UUID, null)
        set(value) = prefs.addString(UUID, value)

}