package com.kpiroom.bubble.util.databinding

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.kpiroom.bubble.util.databinding.ProgressState.Companion.ALERT
import com.kpiroom.bubble.util.databinding.ProgressState.Companion.FINISHED
import com.kpiroom.bubble.util.databinding.ProgressState.Companion.LOADING
import com.kpiroom.bubble.util.view.ProgressLayout

@BindingAdapter("android:layout_height")
fun setLayoutHeight(view: View, newHeight: Float) {
    view.layoutParams = view.layoutParams.apply { height = newHeight.toInt() }
}

@BindingAdapter("app:imeActionDone")
fun isImeActionDone(editText: EditText, isActionDone: Boolean) {
    editText.imeOptions = if (isActionDone) {
        EditorInfo.IME_ACTION_DONE
    } else {
        EditorInfo.IME_ACTION_NEXT
    }
    editText.clearFocus()
}

@BindingAdapter("app:progressState")
fun setProgressState(progressLayout: ProgressLayout, stateContainer: ProgressState?) {

    fun onLoading(message: String?) {
        message?.let {
            progressLayout.progress(it)
        } ?: run {
            progressLayout.progress()
        }
    }

    fun onAlert(message: String, callback: ((Boolean) -> Unit)?) {
        callback?.let {
            progressLayout.alert(message, callback)
        } ?: run {
            progressLayout.alert(message)
        }
    }

    fun onDismissed() {
        progressLayout.dismiss()
    }

    stateContainer?.let {
        val state = it.state
        val loadingMessage = it.message
        val alertMessage = it.message ?: ""
        val callback = it.callback

        when (state) {
            LOADING -> onLoading(loadingMessage)
            ALERT -> onAlert(alertMessage, callback)
            FINISHED -> onDismissed()
        }
    }
}