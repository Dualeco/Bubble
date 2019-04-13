package com.kpiroom.bubble.util.constants

import android.content.res.Resources
import android.os.Build
import android.os.Environment
import com.kpiroom.bubble.R
import com.kpiroom.bubble.os.BubbleApp
import com.kpiroom.bubble.util.files.mkDir
import com.kpiroom.bubble.util.view.getNavBarHeight
import com.kpiroom.bubble.util.view.getStatusBarHeight
import com.kpiroom.bubble.util.view.getToolbarHeight
import java.io.File


val DISPLAY_WIDTH: Int = Resources.getSystem().displayMetrics.widthPixels
val DISPLAY_HEIGHT: Int = Resources.getSystem().displayMetrics.heightPixels
val STATUS_BAR_SIZE: Int
    get() = BubbleApp.app.getStatusBarHeight()
val TOOLBAR_SIZE: Int
    get() = BubbleApp.app.getToolbarHeight()
val NAVBAR_SIZE: Int
    get() = BubbleApp.app.getNavBarHeight()
val CONTAINER_TOP_MARGIN = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) -STATUS_BAR_SIZE else 0

val DIR_PICTURES: File = BubbleApp.app.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
val DIR_CAMERA: File = mkDir(DIR_PICTURES, str(R.string.camera_dir))
val DIR_COMICS: File = mkDir(DIR_PICTURES, str(R.string.comics_dir))

val FILE_PROVIDER = "com.kpiroom.bubble.fileprovider"

fun android(api: Int, action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= api) action()
}

fun androidIf(api: Int, action: () -> Unit, action2: () -> Unit) {
    if (Build.VERSION.SDK_INT >= api) action() else action2()
}

fun <T> androidResult(api: Int, action: () -> T?): T? = if (Build.VERSION.SDK_INT >= api) action() else null
