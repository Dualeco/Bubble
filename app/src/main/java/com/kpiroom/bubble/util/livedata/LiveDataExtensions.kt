package com.kpiroom.bubble.util.livedata

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


fun <T : Any?> MutableLiveData<T>.setDefault(default: T?) = apply { value = default }
fun <T : Any?> swapValues(liveData1: MutableLiveData<T>, liveData2: MutableLiveData<T>) {
    val temp: T? = liveData1.value
    liveData1.value = liveData2.value
    liveData2.value = temp
}

fun MutableLiveData<Boolean>.observeTrue(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<Boolean>) {
    observe(owner, Observer {
        if (it == true)
            observer.onChanged(it)
    })
}

