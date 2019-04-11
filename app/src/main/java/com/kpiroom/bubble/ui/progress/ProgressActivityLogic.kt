package com.kpiroom.bubble.ui.progress

import androidx.lifecycle.MediatorLiveData
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.progressState.ProgressState

abstract class ProgressActivityLogic : CoreLogic() {
    abstract val progress: MediatorLiveData<ProgressState>
}