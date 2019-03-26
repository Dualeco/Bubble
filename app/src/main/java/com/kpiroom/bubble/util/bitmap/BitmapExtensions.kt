package com.kpiroom.bubble.util.bitmap

import android.graphics.Bitmap
import kotlin.math.min

val Bitmap.squareBitmap: Bitmap
    get() {
        val dimension = min(width, height)
        val x = (width - dimension) / 2
        val y = (height - dimension) / 2

        return Bitmap.createBitmap(this, x, y, dimension, dimension) ?: this
    }