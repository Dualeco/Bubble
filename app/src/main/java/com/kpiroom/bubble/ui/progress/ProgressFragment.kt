package com.kpiroom.bubble.ui.progress

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MediatorLiveData
import com.kpiroom.bubble.ui.core.CoreFragment
import com.kpiroom.bubble.util.livedata.addSource
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.livedata.progressState.finish

abstract class ProgressFragment<L : ProgressFragmentLogic, B : ViewDataBinding> : CoreFragment<L, B>() {

    private lateinit var parentProgressMediator: MediatorLiveData<ProgressState>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentProgressMediator = (activity as ProgressMediator).progressMediator
            .apply {
                addSource(logic.progress)
            }
    }

    override fun onDetach() {
        super.onDetach()
        logic.progress.apply {
            finish()
            parentProgressMediator.removeSource(this)
        }
    }
}