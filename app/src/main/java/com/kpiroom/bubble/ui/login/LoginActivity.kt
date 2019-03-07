package com.kpiroom.bubble.ui.login


import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.R
import com.kpiroom.bubble.databinding.ActivityLoginBinding
import com.kpiroom.bubble.ui.core.CoreActivity
import com.kpiroom.bubble.util.view.LoginAnimation
import com.kpiroom.bubble.util.view.hideKeyboard
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : CoreActivity<LoginLogic, ActivityLoginBinding>() {

    private val toggleAuthAnimation = LinkedList<ValueAnimator>()
    private lateinit var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    companion object {
        private const val TAG = "LoginActivity"
        fun getIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val anim = LoginAnimation(storage = toggleAuthAnimation)

        listOf<EditText>(emailEditText, passwordEditText, confirmPasswordEditText)
            .forEach {
                it.setOnTouchListener { _, _ ->
                    if (it.isFocusableInTouchMode) {
                        anim.smoothScrollToBottom(progressLayout, scrollView).start()
                    }
                    false
                }
            }
        changeAuthButton.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        anim.apply {
            transformEditTextAlpha(confirmPasswordEditText, true)
            transformAlpha(confirmPasswordBackground, true)
            onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
                transformHeight(
                    confirmPasswordBackground,
                    forgotPasswordTextView.measuredHeight,
                    emailEditText.measuredHeight
                )
            }
            confirmPasswordEditText.viewTreeObserver
                .addOnGlobalLayoutListener(onGlobalLayoutListener)

            transformTextColor(forgotPasswordTextView, 250L, DecelerateInterpolator())
            transformAlpha(forgotPasswordTextView, false)
            scaleXY(forgotPasswordTextView)
            translateY(
                forgotPasswordTextView,
                forgotPasswordTextView.y,
                forgotPasswordTextView.y + confirmPasswordEditText.paddingTop
            )
        }

        logic.isNewAccount.observe(this, Observer { isNew ->
            isNew?.let {
                toggleAuthAnimation.forEach {
                    if (isNew) {
                        it.start()
                    } else {
                        it.reverse()
                    }
                }
            }
        })

        logic.delayedAction.observe(this, Observer { action ->
            action?.apply {
                Handler().postDelayed(::start, delay)
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.let {
            if (!it.isWithinView(editTextArea) && !it.isWithinView(changeAuthButton))
                hideKeyboard()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun MotionEvent.isWithinView(view: View): Boolean =
        (action == MotionEvent.ACTION_UP).let {
            val srcCoords = IntArray(2)
            view.getLocationOnScreen(srcCoords)

            val x = rawX + view.left - srcCoords[0]
            val y = rawY + view.top - srcCoords[1]

            if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
                return false
            }
            return true
        }

    override fun onDestroy() {
        super.onDestroy()
        confirmPasswordEditText.viewTreeObserver
            .removeOnGlobalLayoutListener(onGlobalLayoutListener)
        toggleAuthAnimation.forEach { it.cancel() }
    }

    override fun provideLogic() = ViewModelProviders.of(this).get(LoginLogic::class.java)

    override fun provideLayout() = LayoutBuilder(R.layout.activity_login) {
        logic = this@LoginActivity.logic
    }
}