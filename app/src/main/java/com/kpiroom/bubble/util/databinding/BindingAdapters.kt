package com.kpiroom.bubble.util.databinding

import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dichotome.profilebar.util.extensions.download
import com.kpiroom.bubble.os.BubbleApp

@BindingAdapter("android:layout_height")
fun setLayoutHeight(view: View, newHeight: Float) {
    view.layoutParams = view.layoutParams.apply { height = newHeight.toInt() }
}

@BindingAdapter("android:layout_marginTop")
fun setLayoutMarginTop(view: View, marginTop: Float) {
    view.layoutParams = (view.layoutParams as ViewGroup.MarginLayoutParams).apply { topMargin = marginTop.toInt() }
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

@BindingAdapter("app:source")
fun setBitmap(imageView: ImageView, source: Any?) {
    source?.let {
        imageView.download(source)
    }
}