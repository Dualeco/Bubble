package com.kpiroom.bubble.os

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.databinding.BindingAdapter

@BindingAdapter("android:layout_height")
fun setLayoutHeight(view: View, height: Float) {
    view.layoutParams = view.layoutParams.apply { this.height = height.toInt() }
}