package com.kpiroom.bubble.ui.main

import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import java.io.File

class MainLogic : CoreLogic() {

    fun fetchUserData() {
        AsyncProcessor {
            Source.apply {
                userPrefs.apply {
                    api.updateUserPrefs(userPrefs.uuid)
                }
            }
        } handleError {
        } runWith (bag)
    }
}