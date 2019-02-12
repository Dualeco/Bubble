package com.kpiroom.bubble.util.view

import android.animation.TimeInterpolator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView

object AnimationUtils {

    const val DURATION = 300L

    fun transformTextColor(
        view: TextView,
        evaluator: TypeEvaluator<Int>,
        colorBegin: Int,
        colorEnd: Int,
        duration: Long = DURATION,
        interpolator: TimeInterpolator = AccelerateInterpolator()
    ) = ValueAnimator.ofObject(evaluator, colorBegin, colorEnd)
        .apply {
            this.duration = duration
            this.interpolator = interpolator
            addUpdateListener {
                view.setTextColor(animatedValue as Int)
            }
        }

    fun transformAlpha(
        view: View,
        alphaBegin: Float,
        alphaEnd: Float,
        duration: Long = DURATION
    ) = ValueAnimator.ofFloat(alphaBegin, alphaEnd)
        .apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                view.alpha = animatedValue as Float
            }
        }

    fun transformHeight(
        view: TextView,
        heightBegin: Int,
        heightEnd: Int,
        duration: Long = 200L
    ) = ValueAnimator.ofInt(heightBegin, heightEnd)
        .apply {
            this.duration = duration
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                view.layoutParams = view.layoutParams.apply {
                    height = animatedValue as Int
                }
            }
        }

    fun scaleXY(
        view: View,
        scaleBegin: Float = 1f,
        scaleEnd: Float,
        duration: Long = DURATION
    ) = ValueAnimator.ofFloat(scaleBegin, scaleEnd)
        .apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                view.scaleX = animatedValue as Float
                view.scaleY = animatedValue as Float
            }
        }

    fun translateY(
        view: View,
        positionBegin: Float,
        positionEnd: Float,
        duration: Long = DURATION
    ) = ValueAnimator.ofFloat(positionBegin, positionEnd)
        .apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                view.y = animatedValue as Float
            }
        }
}