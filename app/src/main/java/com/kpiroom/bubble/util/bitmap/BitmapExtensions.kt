package com.kpiroom.bubble.util.bitmap

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.math.min

val Bitmap.squareBitmap: Bitmap
    get() {
        val dimension = min(width, height)
        val x = (width - dimension) / 2
        val y = (height - dimension) / 2

        return Bitmap.createBitmap(this, x, y, dimension, dimension) ?: this
    }

fun Bitmap.getCompressed(
    quality: Int = 50,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
) = ByteArrayOutputStream().let {
    compress(format, quality, it)
    BitmapFactory.decodeStream(ByteArrayInputStream(it.toByteArray()))
}