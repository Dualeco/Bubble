package com.kpiroom.bubble.ui.main

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.ui.progress.ProgressActivityLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.livedata.progressState.alertAsync
import com.kpiroom.bubble.util.progressState.ProgressState
import java.io.File

class MainLogic : ProgressActivityLogic() {
    fun fetchUserData() {
        AsyncProcessor {
            Source.apply {
                userPrefs.apply {
                    api.updateUserPrefs(userPrefs.uuid)
                }
            }
        } handleError {
            progress.alertAsync(it.message)
        } runWith (bag)
    }
}