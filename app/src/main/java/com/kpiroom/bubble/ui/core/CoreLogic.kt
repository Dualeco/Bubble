package com.kpiroom.bubble.ui.core

import android.os.Handler
import androidx.lifecycle.ViewModel
import com.kpiroom.bubble.util.async.AsyncBag
import com.kpiroom.bubble.util.event.ClickThrottler
import com.kpiroom.bubble.util.event.Debounce

abstract class CoreLogic: ViewModel() {

    val bag = AsyncBag()

    val clickThrottler = ClickThrottler()
    val debounce = Debounce(Handler())

    override fun onCleared() {
        super.onCleared()
        bag.cancel()
    }
}