package com.kpiroom.bubble.ui.comic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.ui.progress.ProgressFragmentLogic
import com.kpiroom.bubble.util.progressState.ProgressState

class ComicLogic : ProgressFragmentLogic() {
    override val progress = MutableLiveData<ProgressState>()
}