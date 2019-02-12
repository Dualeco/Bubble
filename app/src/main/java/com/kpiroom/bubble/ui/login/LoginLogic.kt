package com.kpiroom.bubble.ui.login

import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constant.str
import com.kpiroom.bubble.util.databinding.ProgressState
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseExceptionHelper
import com.kpiroom.bubble.util.event.DelayedAction
import com.kpiroom.bubble.util.livedata.*

class LoginLogic : CoreLogic() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

    val authButtonText = MutableLiveData<String>().setDefault(str(R.string.login_sign_in))
    val changeAuthButtonText = MutableLiveData<String>().setDefault(str(R.string.login_sign_up))
    val isNewAccount = MutableLiveData<Boolean>()

    val delayedAction = MutableLiveData<DelayedAction>()
    val progress = MutableLiveData<ProgressState>()

    fun onAuthClicked() {
        clickThrottler.next {
            delayedAction.value = DelayedAction({
                signUpOrSignIn()
            }, 300)
        }
    }

    fun onForgotPassword() {
        clickThrottler.next {
            email.value?.let {
                AsyncProcessor {
                    Source.api.sendPasswordResetEmail(email.value!!)
                    progress.alert(
                        str(R.string.message_forgot_password_instructions_sent)
                    )
                }.handleError {
                    progress.alert( "${str(R.string.message_forgot_password_error)}: $it")
                }.run(bag)
            } ?: run {
                progress.alert(
                    str(R.string.message_forgot_password_no_email)
                )
            }
        }
    }

    fun toggleNewAccount() {
        swapValues(authButtonText, changeAuthButtonText)

        val newValue = isNewAccount.value != true
        if (!newValue) {
            delayedAction.value = DelayedAction({ confirmPassword.value = "" }, 300L)
        }
        isNewAccount.value = newValue
    }

    private fun signUpOrSignIn() {

        progress.load()

        val email = email.value
        val password = password.value
        val confirmPassword = confirmPassword.value

        val isNewAccount = isNewAccount.value

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            progress.alert(str(R.string.message_auth_fields_empty))
            return

        } else if (isNewAccount == true && confirmPassword.isNullOrEmpty()) {
            progress.alert(str(R.string.message_auth_confirm_password))
            return
        } else if (isNewAccount == true && confirmPassword != password) {
            progress.alert(str(R.string.message_auth_passwords_do_not_match))
            return
        }


        AsyncProcessor {
            Source.userPrefs.uuid =
                if (isNewAccount == true) {
                    Source.api.signUp(email, password)
                } else {
                    Source.api.signIn(email, password)
                }
            progress.finishAsync()
        }.handleError {
            progress.alertAsync(FirebaseExceptionHelper.interpretException(it))
        }.run(bag)
    }
}