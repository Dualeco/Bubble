package com.kpiroom.bubble.ui.progress

import androidx.lifecycle.MediatorLiveData
import com.kpiroom.bubble.util.progressState.ProgressState

interface ProgressMediator {
    var progressMediator: MediatorLiveData<ProgressState>
}