package com.kpiroom.bubble.ui.progress

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MediatorLiveData
import com.kpiroom.bubble.ui.core.CoreActivity
import com.kpiroom.bubble.util.progressState.ProgressState

abstract class ProgressActivity<L : ProgressActivityLogic, B : ViewDataBinding> : CoreActivity<L, B>(), ProgressMediator {
    override lateinit var progressMediator: MediatorLiveData<ProgressState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressMediator = logic.progress
    }
}