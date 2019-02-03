package com.kpiroom.bubble.util.databinding

import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.kpiroom.bubble.util.databinding.ProgressState.*
import com.kpiroom.bubble.util.view.ProgressLayout

@BindingAdapter("android:layout_height")
fun setLayoutHeight(view: View, height: Float) {
    view.layoutParams = view.layoutParams.apply { this.height = height.toInt() }
}

@BindingAdapter("app:imeActionDone")
fun isImeActionDone(editText: EditText, isActionDone: Boolean) {
    Log.d("Adapter", isActionDone.toString())
    if (isActionDone) {
        editText.imeOptions = EditorInfo.IME_ACTION_DONE
    } else {
        editText.imeOptions = EditorInfo.IME_ACTION_NEXT
    }
    editText.clearFocus()
}

@BindingAdapter("app:progressState")
fun setProgressState(progressLayout: ProgressLayout, stateContainer: ProgressStateContainer?) {

    if (stateContainer == null) {
        return
    }

    val state = stateContainer.state
    val loadingMessage = stateContainer.message
    val alertMessage = stateContainer.message ?: "ERROR: MESSAGE MUST BE SPECIFIED"
    val callback = stateContainer.callback

    fun onLoading(message: String?) {
        if (message != null) {
            progressLayout.progress(message)
        } else {
            progressLayout.progress()
        }
    }

    fun onAlert(message: String, callback: ((Boolean) -> Unit)?) {
        if (callback == null) {
            progressLayout.alert(message)
        } else {
            progressLayout.alert(message, callback)
        }
    }

    fun onDismissed() {
        progressLayout.dismiss()
    }

    when (state) {
        LOADING -> onLoading(loadingMessage)
        ALERT -> onAlert(alertMessage, callback)
        FINISHED -> onDismissed()
    }
}