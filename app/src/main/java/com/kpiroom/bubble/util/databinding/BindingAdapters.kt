package com.kpiroom.bubble.util.databinding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("android:layout_height")
fun setLayoutHeight(view: View, height: Float) {
    view.layoutParams = view.layoutParams.apply { this.height = height.toInt() }
}