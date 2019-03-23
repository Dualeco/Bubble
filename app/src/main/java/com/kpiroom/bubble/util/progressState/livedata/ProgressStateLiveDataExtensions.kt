package com.kpiroom.bubble.util.progressState.livedata

import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.util.progressState.ProgressState

fun MutableLiveData<ProgressState>.alert(
    message: String? = null,
    callback: ((Boolean) -> Unit)? = null,
    firstOption: String? = null,
    secondOption: String? = null
) {

    value = ProgressState(
        ProgressState.ALERT,
        message,
        callback,
        firstOption,
        secondOption
    )
}

fun MutableLiveData<ProgressState>.alertAsync(
    message: String? = null,
    callback: ((Boolean) -> Unit)? = null,
    firstOption: String? = null,
    secondOption: String? = null
) {
    postValue(
        ProgressState(
            ProgressState.ALERT,
            message,
            callback,
            firstOption,
            secondOption
        )
    )
}

fun MutableLiveData<ProgressState>.finish() {
    value = ProgressState(
        ProgressState.FINISHED
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
    value = ProgressState(
        ProgressState.LOADING,
        message
    )
}

fun MutableLiveData<ProgressState>.loadAsync(message: String? = null) {
    postValue(
        ProgressState(
            ProgressState.LOADING,
            message
        )
    )
}

fun MutableLiveData<ProgressState>.input(callback: ((String) -> Unit)) {
    value = ProgressState(
        ProgressState.INPUT,
        inputCallback = callback
    )
}

