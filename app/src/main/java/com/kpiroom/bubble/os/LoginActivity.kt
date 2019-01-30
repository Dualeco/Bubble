package com.kpiroom.bubble.os

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.ActivityLoginScreenBinding
import com.kpiroom.bubble.util.view.hideKeyboard
import kotlinx.android.synthetic.main.activity_login_screen.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    private val animatorCollector = LinkedList<Animator>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityLoginScreenBinding>(this, R.layout.activity_login_screen)

        val everyEditText = listOf<EditText>(emailEditText, passwordEditText)
        everyEditText.forEach {
            it.setOnTouchListener { _, _ ->
                smoothScrollTo(scrollView)
                false
            }
        }

        getStartedButton.setOnClickListener {
            hideKeyboard()
            progressLayout.progress()
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.setsFocusInView(currentFocus) != true) {
            hideKeyboard()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun MotionEvent.setsFocusInView(view: View?): Boolean {
        if (view != null &&
                view.isFocusable &&
                (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_MOVE) &&
                (view.id in listOf(emailEditText.id, passwordEditText.id))) {

            val srcCoords = IntArray(2)
            view.getLocationOnScreen(srcCoords)

            val x = rawX + view.left - srcCoords[0]
            val y = rawY + view.top - srcCoords[1]

            if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
                return true
            }
        }
        return false
    }

    private fun smoothScrollTo(view: View) {
        val rect = Rect()
        view.getGlobalVisibleRect(rect)

        ValueAnimator.ofInt(scrollView.scrollY, rect.bottom + scrollView.scrollY)
                .apply {
                    animatorCollector.add(this)
                    interpolator = AccelerateDecelerateInterpolator()
                    addUpdateListener {
                        scrollView.smoothScrollTo(0, it.animatedValue as Int)
                    }
                }
                .start()


    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animatorCollector.forEach { it.cancel() }
    }
}
