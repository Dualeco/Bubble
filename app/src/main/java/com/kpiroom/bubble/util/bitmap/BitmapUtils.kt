package com.kpiroom.bubble.util.bitmap

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.kpiroom.bubble.os.BubbleApp

fun extractBitmapFrom(uri: Uri): Bitmap? = MediaStore.Images.Media.getBitmap(BubbleApp.app.contentResolver, uri)