package com.kpiroom.bubble.os

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.kpiroom.bubble.R
import kotlinx.android.synthetic.main.layout_dialog.view.*

class ProgressLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) :
    FrameLayout(context, attrs, defStyle) {

    companion object {

        const val ANIMATION_TIME = 200L

        const val LOADING_ANIMATION_TIME = 400L
    }

    private val container = FrameLayout(context)

    var isLoading = false

    private val loading = LayoutInflater.from(context).inflate(R.layout.layout_loading_background, this, false).apply {
        layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                    .apply {
                        gravity = Gravity.BOTTOM
                    }
        translationY = (DISPLAY_HEIGHT / 2).toFloat()
    }

    private val staticBackground = ImageView(context).apply {
        layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                    .apply {
                        visibility = View.INVISIBLE
                        gravity = Gravity.BOTTOM
                    }
        scaleType = ImageView.ScaleType.FIT_XY
        setImageResource(R.drawable.ic_bottom_waves)
    }

    private val dimmingView = View(context).apply {
        setBackgroundColor(bindColor(R.color.dimColor))
        layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        alpha = 0F
    }

    private val alert = LayoutInflater.from(context).inflate(R.layout.layout_dialog, this, false).apply {
        layoutParams =
                FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                    .apply {
                        gravity = Gravity.BOTTOM
                    }
        translationY = (DISPLAY_HEIGHT / 2).toFloat()
    }

    init {
        addView(container)
        addView(staticBackground)
        addView(dimmingView)
        addView(loading)
        addView(alert)
    }

    fun progress(message: String = bindString(R.string.common_loading)) {
        isLoading = true
        dimmingView.isClickable = true

        updateStaticBack(false)
        updateDimming(true)
        updateLoading(true, message)
        updateAlert(false)

        Handler().postDelayed({
            alert("Are you ready to do this?") {
                if (it) {
                    progress()
                } else {
                    content()
                }
            }
        }, 3000L)
    }

    fun content() {
        isLoading = false
        dimmingView.isClickable = false

        updateDimming(false)
        updateStaticBack(true)
        updateLoading(false)
        updateAlert(false)
    }

    fun alert(message: String, callback: ((Boolean) -> Unit)? = null) {
        isLoading = true
        dimmingView.isClickable = true

        updateDimming(true)
        updateLoading(false)
        updateStaticBack(false)
        updateAlert(true, message, callback)
    }

    private fun updateAlert(show: Boolean, message: String = "", callback: ((Boolean) -> Unit)? = null) {
        alert.apply {
            if (show) {
                findViewById<TextView>(R.id.text).text = message
                if (callback != null) {
                    findViewById<Button>(R.id.firstButton).apply {
                        text = bindString(R.string.common_yes)
                        setOnClickListener { callback(true) }
                    }
                    findViewById<Button>(R.id.secondButton).apply {
                        text = bindString(R.string.common_no)
                        visibility = View.VISIBLE
                        setOnClickListener { callback(false) }
                    }
                } else {
                    findViewById<Button>(R.id.firstButton).apply {
                        text = bindString(R.string.common_ok)
                        setOnClickListener { content() }
                    }
                }
            }
        }.animate()
            .alpha(if (show) 1F else 0F)
            .translationY(if (show) 0F else staticBackground.height.toFloat())
            .setDuration(ANIMATION_TIME)
            .setInterpolator(if (show) DecelerateInterpolator() else AccelerateInterpolator())
    }

    private fun updateStaticBack(show: Boolean) {
        staticBackground.animate()
            .alpha(if (show) 1F else 0F)
            .translationY(if (show) 0F else staticBackground.height.toFloat())
            .setDuration(ANIMATION_TIME)
            .setInterpolator(if (show) DecelerateInterpolator() else AccelerateInterpolator())
    }

    private fun updateLoading(show: Boolean, message: String = "") {
        if (message.isNotEmpty()) loading.findViewById<TextView>(R.id.loading).text = message
        loading.animate()
            .translationY(if (show) 0F else (DISPLAY_HEIGHT / 2).toFloat())
            .setDuration(LOADING_ANIMATION_TIME)
            .setInterpolator(if (show) DecelerateInterpolator() else AccelerateInterpolator())
    }

    private fun updateDimming(show: Boolean) {
        dimmingView.animate()
            .alpha(if (show) 0.5F else 0F)
            .setDuration(ANIMATION_TIME)
            .setInterpolator(AccelerateDecelerateInterpolator())
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child == container || child == dimmingView || child == loading || child == alert || child == staticBackground) {
            super.addView(child, index, params)
        } else {
            container.addView(child, index, params)
        }
    }
}