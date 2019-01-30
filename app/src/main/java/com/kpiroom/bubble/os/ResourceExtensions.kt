package com.kpiroom.bubble.os

import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.TypedValue
import androidx.annotation.*
import androidx.core.content.ContextCompat


fun bindString(@StringRes res: Int): String = BubbleApp.app.getString(res)

fun bindString(@StringRes res: Int, arg1: String): String = BubbleApp.app.getString(res, arg1)
fun bindString(@StringRes res: Int, arg1: Int): String = BubbleApp.app.getString(res, arg1)
fun bindString(@StringRes res: Int, arg1: String, arg2: String): String = BubbleApp.app.getString(res, arg1, arg2)

fun bindPlural(@PluralsRes res: Int, amount: Int, param: Int = amount): String =
        BubbleApp.app.resources.getQuantityString(res, amount, param)

fun bindInt(@IntegerRes res: Int): Int = BubbleApp.app.resources.getInteger(res)

fun bindDimen(@DimenRes res: Int): Float = BubbleApp.app.resources.getDimension(res)

fun bindColor(@ColorRes res: Int): Int = ContextCompat.getColor(BubbleApp.app, res)

fun bindDrawable(@DrawableRes res: Int): Drawable = ContextCompat.getDrawable(BubbleApp.app, res)
        ?: ShapeDrawable()

fun dpToPx(dp: Int): Int = dpToPx(dp.toFloat())

fun dpToPx(dp: Float): Int = Math.round(dp * BubbleApp.app.resources.displayMetrics.density)

fun spToPx(sp: Int): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp.toFloat(), BubbleApp.app.resources.displayMetrics
)

fun displayMetrics() = BubbleApp.app.resources.displayMetrics

fun measureHeight(text: CharSequence, textPaint: TextPaint, width: Int): Int {
    return StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, 1F, 0F, true).height
}