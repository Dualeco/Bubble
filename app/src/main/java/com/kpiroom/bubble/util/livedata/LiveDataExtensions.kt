package com.kpiroom.bubble.util.livedata

import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.util.databinding.ProgressState


fun <T : Any?> MutableLiveData<T>.setDefault(default: T) = apply { value = default }
fun <T : Any?> swapValues(liveData1: MutableLiveData<T>, liveData2: MutableLiveData<T>) {
    val temp: T? = liveData1.value
    liveData1.value = liveData2.value
    liveData2.value = temp
}

fun MutableLiveData<ProgressState>.alert(message: String? = null, callback: ((Boolean) -> Unit)? = null) {
    value = ProgressState(ProgressState.ALERT, message, callback)
}

fun MutableLiveData<ProgressState>.alertAsync(message: String? = null, callback: ((Boolean) -> Unit)? = null) {
    postValue(
        ProgressState(
            ProgressState.ALERT,
            message,
            callback
        )
    )
}

fun MutableLiveData<ProgressState>.finishAsync() {
    postValue(
        ProgressState(
            ProgressState.FINISHED
        )
    )
}

fun MutableLiveData<ProgressState>.load(message: String? = null) {
    value = ProgressState(ProgressState.LOADING, message)
}

fun MutableLiveData<ProgressState>.loadAsync(message: String? = null) {
    postValue(
        ProgressState(
            ProgressState.LOADING,
            message
        )
    )
}

