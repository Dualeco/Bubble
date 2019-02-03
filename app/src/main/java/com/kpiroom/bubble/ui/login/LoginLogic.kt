package com.kpiroom.bubble.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseApi
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseAuthUtil
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseExceptionHelper
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.constant.str
import com.kpiroom.bubble.util.databinding.ProgressState
import com.kpiroom.bubble.util.databinding.ProgressState.*
import com.kpiroom.bubble.util.databinding.ProgressStateContainer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DelayedAction(val action: () -> Unit, val delay: Long)
class LoginLogic : CoreLogic() {

    companion object {
        const val TAG = "LoginLogic"
    }

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    val authButtonText = MutableLiveData<String>().setDefault(str(R.string.login_sign_in))
    val changeAuthButtonText = MutableLiveData<String>().setDefault(str(R.string.login_sign_up))
    val isNewAccount = MutableLiveData<Boolean>()

    val delayedAction = MutableLiveData<DelayedAction>()
    val authProgressState = MutableLiveData<ProgressStateContainer>()

    val api = FirebaseApi()


    fun onAuthClicked() {
        delayedAction.value = DelayedAction(onAuth, 300)
    }

    fun onForgotPassword() {
        val email = email.value

        if (email.isNullOrEmpty()) {
            authProgressState.setValue(ALERT, "To reset your password, please specify the email")
            return
        }


        GlobalScope.launch {
            try {
                api.sendPasswordResetEmail(email)
                authProgressState.postValue(
                    ALERT,
                    "Instructions have been sent to your email address. Please, check your inbox to reset the password"
                )
            } catch (ex: Exception) {
                authProgressState.postValue(ALERT, "Error sending the instructions by email: $ex")
            }
        }


    }

    fun toggleNewAccount() {
        val newValue = isNewAccount.value != true
        if (!newValue) {
            delayedAction.value = DelayedAction({ confirmPassword.value = "" }, 300)
        }
        isNewAccount.value = newValue
    }

    private fun signUpOrSignIn() {

        authProgressState.setValue(LOADING)

        val email = email.value
        val password = password.value
        val confirmPassword = confirmPassword.value

        val isNewAccount = isNewAccount.value

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            authProgressState.setValue(ALERT, "Email and password fields must not be empty")
            return

        } else if (isNewAccount == true && confirmPassword.isNullOrEmpty()) {
            authProgressState.setValue(ALERT, "Please, confirm your password")
            return
        } else if (isNewAccount == true && confirmPassword != password) {
            authProgressState.setValue(ALERT, "Entered passwords do not match")
            return
        }

        fun interpretException(ex: Exception): String? {
            val helper = FirebaseExceptionHelper()

            return helper.interpretException(ex)
        }

        GlobalScope.launch {
            try {
                if (isNewAccount == true) {
                    Source.userPrefs.uuid = api.signUp(email, password)
                } else {
                    Source.userPrefs.uuid = api.signIn(email, password)
                }
                authProgressState.postValue(FINISHED)
            } catch (ex: Exception) {
                authProgressState.postValue(ALERT, interpretException(ex))
                Log.d(FirebaseAuthUtil.TAG, ex.toString())
            }
        }

    }

    val onAuth: () -> Unit = {
        clickThrottler.next {
            signUpOrSignIn()
        }
    }
}


fun <T : Any?> MutableLiveData<T>.setDefault(default: T) = apply { value = default }

fun MutableLiveData<ProgressStateContainer>.setValue(
    state: ProgressState,
    message: String? = null,
    callback: ((Boolean) -> Unit)? = null
) {
    val progressStateContainer = ProgressStateContainer(state, message, callback)
    value = progressStateContainer
}

fun MutableLiveData<ProgressStateContainer>.postValue(
    state: ProgressState,
    message: String? = null,
    callback: ((Boolean) -> Unit)? = null
) {
    val progressStateContainer = ProgressStateContainer(state, message, callback)
    postValue(progressStateContainer)
}