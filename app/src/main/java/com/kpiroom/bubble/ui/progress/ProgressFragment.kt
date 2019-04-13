package com.kpiroom.bubble.ui.progress

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MediatorLiveData
import com.kpiroom.bubble.ui.core.CoreFragment
import com.kpiroom.bubble.util.livedata.addSource
import com.kpiroom.bubble.util.livedata.progressState.finish
import com.kpiroom.bubble.util.progressState.ProgressState

abstract class ProgressFragment<L : ProgressFragmentLogic, B : ViewDataBinding> : CoreFragment<L, B>() {

    private lateinit var parentProgressMediator: MediatorLiveData<ProgressState>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        parentProgressMediator = (activity as ProgressMediator).progressMediator.apply {
            addSource(logic.progress)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        logic.progress.apply {
            finish()
            parentProgressMediator.removeSource(this)
        }
    }
}