package com.kpiroom.bubble.ui.progress

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.progressState.ProgressState

abstract class ProgressFragmentLogic : CoreLogic() {
    abstract val progress: MutableLiveData<ProgressState>
}