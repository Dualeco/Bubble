package com.kpiroom.bubble.util.databinding

import android.net.Uri
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profileshared.views.CircularImageView
import com.kpiroom.bubble.os.BubbleApp
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

    fun onAlert(
        message: String,
        callback: ((Boolean) -> Unit)?,
        firstOption: String? = null,
        secondOption: String? = null
    ) {
        callback?.let {
            progressLayout.alert(message, callback, firstOption, secondOption)
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

        val firstOption = it.firstOption
        val secondOption = it.secondOption

        when (state) {
            LOADING -> onLoading(loadingMessage)
            ALERT -> onAlert(alertMessage, callback, firstOption, secondOption)
            FINISHED -> onDismissed()
        }
    }
}

@BindingAdapter("app:uri")
fun setUri(imageView: ImageView, uri: Uri?) {
    uri?.let {
        val options = RequestOptions().apply {
            if (imageView is CircularImageView)
                circleCrop()
        }
        Glide.with(BubbleApp.app)
            .load(uri)
            .apply(options)
            .into(imageView)
    }
}