package com.kpiroom.bubble.ui.main

import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor

class MainLogic : CoreLogic() {
    fun fetchUserData() {
        AsyncProcessor {
            Source.apply {
                userPrefs.uuid?.let {
                    api.fetchUserPrefs(it)
                }
            }
        } runWith(bag)
    }
}