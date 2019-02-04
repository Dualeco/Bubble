package com.kpiroom.bubble.ui.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseAuthUtil
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseExceptionHelper
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncBag
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constant.str
import com.kpiroom.bubble.util.databinding.ProgressState
import com.kpiroom.bubble.util.databinding.ProgressState.*
import com.kpiroom.bubble.util.databinding.ProgressStateContainer
import com.kpiroom.bubble.R

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

    val api = Source.api

    val asyncJobs = AsyncBag()

    fun onAuthClicked() {
        clickThrottler.next {
            delayedAction.value = DelayedAction({
                signUpOrSignIn()
            }, 300)
        }
    }

    fun onForgotPassword() {
        clickThrottler.next {
            val email = email.value

            if (email.isNullOrEmpty()) {
                authProgressState.setValue(ALERT, str(R.string.message_forgot_password_no_email))
                return@next
            }


            AsyncProcessor {
                api.sendPasswordResetEmail(email)
                authProgressState.postValue(
                    ALERT,
                    str(R.string.message_forgot_password_instructions_sent)
                )
            }.handleError {
                authProgressState.postValue(ALERT, "${str(R.string.message_forgot_password_error)}: $it")
            }.run(asyncJobs)
        }
    }

    fun toggleNewAccount() {
        swapValues(authButtonText, changeAuthButtonText)

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
            authProgressState.setValue(ALERT, str(R.string.message_auth_fields_empty))
            return

        } else if (isNewAccount == true && confirmPassword.isNullOrEmpty()) {
            authProgressState.setValue(ALERT, str(R.string.message_auth_confirm_password))
            return
        } else if (isNewAccount == true && confirmPassword != password) {
            authProgressState.setValue(ALERT, str(R.string.message_auth_passwords_do_not_match))
            return
        }

        AsyncProcessor {
            if (isNewAccount == true) {
                Source.userPrefs.uuid = api.signUp(email, password)
            } else {
                Source.userPrefs.uuid = api.signIn(email, password)
            }
            authProgressState.postValue(FINISHED)
        }.handleError {
            authProgressState.postValue(ALERT, FirebaseExceptionHelper().interpretException(it))
            Log.d(FirebaseAuthUtil.TAG, it.toString())
        }.run(asyncJobs)
    }
}

class DelayedAction(val action: () -> Unit, val delay: Long)

fun <T : Any?> MutableLiveData<T>.setDefault(default: T) = apply { value = default }
fun <T : Any?> swapValues(liveData1: MutableLiveData<T>, liveData2: MutableLiveData<T>) {
    val temp: T? = liveData1.value
    liveData1.value = liveData2.value
    liveData2.value = temp
}


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