package com.kpiroom.bubble.util.view

import android.content.Context
import android.util.AttributeSet
import com.dichotome.roundedimageview.RoundedImageView

open class CircularImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RoundedImageView(context, attrs, defStyle) {
    init {
        isCircular = true
    }
}