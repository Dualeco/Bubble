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
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.progressState.ProgressState.Companion.ALERT
import com.kpiroom.bubble.util.progressState.ProgressState.Companion.FINISHED
import com.kpiroom.bubble.util.progressState.ProgressState.Companion.LOADING
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