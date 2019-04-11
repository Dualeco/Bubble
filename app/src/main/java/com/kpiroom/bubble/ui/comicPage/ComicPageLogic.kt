package com.kpiroom.bubble.ui.comicPage

import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.ui.progress.ProgressFragmentLogic
import com.kpiroom.bubble.util.progressState.ProgressState

class ComicPageLogic : ProgressFragmentLogic() {
    override val progress = MutableLiveData<ProgressState>()
}