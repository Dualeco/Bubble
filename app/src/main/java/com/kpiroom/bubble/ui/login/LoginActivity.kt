package com.kpiroom.bubble.ui.login

import android.animation.ValueAnimator
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.kpiroom.bubble.databinding.ActivityLoginBinding
import com.kpiroom.bubble.ui.core.CoreActivity
import com.kpiroom.bubble.util.constant.str
import com.kpiroom.bubble.util.view.LoginAnimation
import com.kpiroom.bubble.util.view.hideKeyboard
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : CoreActivity<LoginLogic, ActivityLoginBinding>() {

    private val toggleAuthAnimation = LinkedList<ValueAnimator>()
    private lateinit var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    companion object {
        const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val anim = LoginAnimation(storage = toggleAuthAnimation)

        val everyEditText = listOf<EditText>(emailEditText, passwordEditText, confirmPasswordEditText)
        everyEditText.forEach {
            it.setOnTouchListener { _, _ ->
                if (it.isFocusableInTouchMode) {
                    anim.smoothScrollToBottom(scrollView, scrollView).start()
                }
                false
            }
        }
        changeAuthButton.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        anim.transformEditTextAlpha(confirmPasswordEditText, true)
        anim.transformAlpha(confirmPasswordBackground, true)
        onGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            anim.transformHeight(
                confirmPasswordBackground,
                forgotPasswordTextView.measuredHeight,
                emailEditText.measuredHeight
            )
        }
        confirmPasswordEditText.viewTreeObserver
            .addOnGlobalLayoutListener(onGlobalLayoutListener)
        anim.transformTextColor(forgotPasswordTextView, 250L, DecelerateInterpolator())
        anim.transformAlpha(forgotPasswordTextView, false)
        anim.scaleXY(forgotPasswordTextView)
        anim.translateY(
            forgotPasswordTextView,
            forgotPasswordTextView.y,
            forgotPasswordTextView.y + confirmPasswordEditText.paddingTop
        )

        logic.isNewAccount.observe(this, Observer { isNew ->
            if (isNew == null) {
                return@Observer
            }

            if (isNew) {
                toggleAuthAnimation.forEach { it.start() }

                logic.authButtonText.value = str(R.string.login_sign_up)
                logic.changeAuthButtonText.value = str(R.string.login_sign_in)
            } else {
                toggleAuthAnimation.forEach { it.reverse() }

                logic.authButtonText.value = str(R.string.login_sign_in)
                logic.changeAuthButtonText.value = str(R.string.login_sign_up)
            }
        })

        logic.delayedAction.observe(this, Observer { action ->
            if (action == null) {
                return@Observer
            }
            Handler().postDelayed({ action.action() }, action.delay)
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null) {
            return false
        }
        if (!(ev.isWithinView(editTextArea) || ev.isWithinView(changeAuthButton))) {
            hideKeyboard()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun MotionEvent.isWithinView(view: View): Boolean {
        if (action in listOf(MotionEvent.ACTION_UP)) {

            val srcCoords = IntArray(2)
            view.getLocationOnScreen(srcCoords)

            val x = rawX + view.left - srcCoords[0]
            val y = rawY + view.top - srcCoords[1]

            if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
                return false
            }
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