package com.kpiroom.bubble.util.files

import android.graphics.Bitmap
import java.io.File
import java.io.FileOutputStream

fun File.putCompressedBitmap(bitmap: Bitmap, format: Bitmap.CompressFormat, quality: Int): Unit =
    FileOutputStream(this).let {
        bitmap.compress(format, quality, it)
        it.flush()
        it.close()
    }
