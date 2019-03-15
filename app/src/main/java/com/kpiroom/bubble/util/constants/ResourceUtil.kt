package com.kpiroom.bubble.util.constants

import android.content.ContentResolver
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.net.Uri
import androidx.annotation.AnyRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.kpiroom.bubble.os.BubbleApp


fun str(@StringRes res: Int): String = BubbleApp.app.getString(res)

fun str(@StringRes res: Int, vararg args: String): String = BubbleApp.app.getString(res, *args)

fun col(@ColorRes res: Int): Int = ContextCompat.getColor(BubbleApp.app, res)

fun drw(@DrawableRes res: Int): Drawable = ContextCompat.getDrawable(BubbleApp.app, res)
    ?: ShapeDrawable()

fun dpToPx(dp: Int): Int = dpToPx(dp.toFloat())

fun dpToPx(dp: Float): Int = Math.round(dp * BubbleApp.app.resources.displayMetrics.density)

fun getResUri(@AnyRes resId: Int) = BubbleApp.app.resources.let {
    Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + it.getResourcePackageName(resId) +
                "/" + it.getResourceTypeName(resId) +
                "/" + it.getResourceEntryName(resId)
    )
}