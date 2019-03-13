package com.kpiroom.bubble.util.pref

import android.content.SharedPreferences


fun SharedPreferences.addString(key: String?, value: String?) {
    edit()
        .putString(key, value)
        .apply()
}