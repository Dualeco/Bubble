package com.kpiroom.bubble.os

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.ActivityLoginScreenBinding
import kotlinx.android.synthetic.main.activity_login_screen.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val animatorCollector = LinkedList<Animator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginScreenBinding>(this, R.layout.activity_login_screen)

        emailEditText.setOnTouchListener { _, _ ->
            scrollTo(scrollView)
            false
        }

        passwordEditText.setOnTouchListener { _, _ ->
            scrollTo(scrollView)
            false
        }

        getStartedButton.setOnClickListener {
            hideKeyboard()
            progressLayout.progress()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val view = currentFocus
        if (view != null &&
                (ev?.action == MotionEvent.ACTION_UP ||
                ev?.action == MotionEvent.ACTION_MOVE) &&
                !view.javaClass.name.startsWith("android.webkit.")) {
            val srcCoords = IntArray(2)
            view.getLocationOnScreen(srcCoords)
            val x = ev.rawX + view.left - srcCoords[0]
            val y = ev.rawY + view.top - srcCoords[1]

            if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
                hideKeyboard()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun scrollTo(view: View) {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)

        ValueAnimator.ofInt(scrollView.scrollY, rect.bottom + scrollView.scrollY)
            .apply {
                addUpdateListener {
                    scrollView.scrollTo(0, it.animatedValue as Int)
                }

                interpolator = AccelerateDecelerateInterpolator()
                addToList()
                start()
            }
    }

    private fun ValueAnimator.addToList() = this.apply { animatorCollector.add(this) }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animatorCollector.forEach { it.cancel() }
    }
}
