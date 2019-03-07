package com.kpiroom.bubble.ui.login


import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.databinding.ProgressState
import com.kpiroom.bubble.util.events.DelayedAction
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
            delayedAction.value = DelayedAction(300L) {
                signUpOrSignIn()
            }
        }
    }

    fun onForgotPassword() {
        clickThrottler.next {
            progress.apply {

                email.value?.let { mail ->

                    AsyncProcessor {
                        Source.api.sendPasswordResetEmail(mail)
                        alert(str(R.string.message_forgot_password_success))
                    } handleError {
                        alert("Error resetting password: ${it.message}")
                    } runWith (bag)

                } ?: run {
                    alert(str(R.string.message_forgot_password_no_email))
                }
            }
        }
    }

    fun toggleNewAccount() {
        clickThrottler.next {
            swapValues(authButtonText, changeAuthButtonText)

            (isNewAccount.value ?: false).let {
                if (it) delayedAction.value = DelayedAction(300L) {
                    confirmPassword.value = ""
                }
                isNewAccount.value = !it
            }
        }
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
            Source.apply {
                userPrefs.uuid = authenticate(
                    if (isNewAccount == true)
                        api::signUp
                    else
                        api::signIn,
                    email,
                    password
                )
            }
            progress.finishAsync()
        } handleError {
            progress.alertAsync(it.message)
        } runWith (bag)
    }

    private suspend fun authenticate(
        authMethod: suspend (String, String) -> String?,
        email: String,
        password: String
    ): String? = authMethod(email, password)
}