package com.kpiroom.bubble.ui.login

import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.ui.core.CoreLogic

class LoginLogic: CoreLogic() {

    val login = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private fun signUpOrSignIn() {
        //TODO("Implement db request for signin or signup")
        //login.value
        //password.value
    }

    fun onGetStarted() {
        clickThrottler.next {
            signUpOrSignIn()
        }
    }
}