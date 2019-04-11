package com.kpiroom.bubble.ui.login


import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.R
import com.kpiroom.bubble.os.BubbleApp
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.events.DelayedAction
import com.kpiroom.bubble.util.livedata.setDefault
import com.kpiroom.bubble.util.livedata.swapValues
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.livedata.progressState.alert
import com.kpiroom.bubble.util.livedata.progressState.alertAsync
import com.kpiroom.bubble.util.livedata.progressState.finishAsync
import com.kpiroom.bubble.util.livedata.progressState.loadAsync

class LoginLogic : CoreLogic() {

    companion object {
        val TAG = "LoginLogic"
    }

    val loggedIn = MutableLiveData<Boolean>()
    val accountSetupRequested = MutableLiveData<Boolean>()

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
                submitData()
            }
        }
    }

    fun onForgotPassword() {
        clickThrottler.next {
            progress.apply {
                email.value.let { mail ->
                    if (mail.isNullOrBlank())
                        alert(str(R.string.message_forgot_password_no_email))
                    else
                        AsyncProcessor {
                            loadAsync()
                            Source.api.sendPasswordResetEmail(mail)
                            alertAsync(str(R.string.message_forgot_password_success))
                        } handleError {
                            val resources = BubbleApp.app.resources
                            alert(resources.getString(R.string.login_error_resetting_password, it.message))
                        } runWith (bag)
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

    private fun submitData() {

        val email = email.value
        val password = password.value
        val confirmPassword = confirmPassword.value

        val isNewAccount = isNewAccount.value ?: false

        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            progress.alert(str(R.string.message_auth_fields_empty))
            return
        } else if (isNewAccount)
            when {
                confirmPassword.isNullOrEmpty() -> {
                    progress.alert(str(R.string.message_auth_confirm_password))
                }
                confirmPassword != password -> {
                    progress.alert(str(R.string.message_auth_passwords_do_not_match))
                }
            }

        authenticate(email, password, isNewAccount)
    }

    private fun authenticate(email: String, password: String, isNewAccount: Boolean) {
        AsyncProcessor {
            progress.loadAsync()
            processAccount(
                if (isNewAccount) ::signUp else ::signIn,
                email,
                password
            )
            progress.finishAsync()
        } handleError {
            progress.alertAsync(it.message)
        } runWith (bag)
    }

    private suspend fun signIn(email: String, password: String) {
        Source.apply {
            userPrefs.apply {
                uuid = api.signIn(email, password) ?: ""
                username = api.getUsername(uuid) ?: ""
                if (username.isBlank())
                    accountSetupRequested.postValue(true)
                else
                    loggedIn.postValue(true)
            }
        }
    }

    suspend fun signUp(email: String, password: String) {
        Source.userPrefs.uuid = Source.api.signUp(email, password) ?: return
        accountSetupRequested.postValue(true)
    }

    private suspend fun processAccount(
        method: suspend (String, String) -> Unit,
        email: String,
        password: String
    ) = method(email, password)
}