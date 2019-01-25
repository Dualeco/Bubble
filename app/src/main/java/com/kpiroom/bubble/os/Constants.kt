package com.kpiroom.bubble.os

import android.content.res.Resources
import android.os.Build


val DISPLAY_WIDTH: Int = Resources.getSystem().displayMetrics.widthPixels
val DISPLAY_HEIGHT: Int = Resources.getSystem().displayMetrics.heightPixels
val STATUS_BAR_SIZE: Int
    get() = BubbleApp.app.getStatusBarHeight()
val TOOLBAR_SIZE: Int
    get() = BubbleApp.app.getToolbarHeight()
val NAVBAR_SIZE: Int
    get() = BubbleApp.app.getNavBarHeight()
val CONTAINER_TOP_MARGIN = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) -STATUS_BAR_SIZE else 0

fun android(api: Int, action: () -> Unit) {
    if (Build.VERSION.SDK_INT >= api) action()
}

fun androidIf(api: Int, action: () -> Unit, action2: () -> Unit) {
    if (Build.VERSION.SDK_INT >= api) action() else action2()
}

fun <T> androidResult(api: Int, action: () -> T?): T? = if (Build.VERSION.SDK_INT >= api) action() else null
