package com.kpiroom.bubble.util.exceptions

import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.constants.str

class CoreException(errorId: Int) : Exception() {
    override var message: String? = ""

    companion object {
        const val DEFAULT: Int = 0
        const val AUTH_INVALID_CREDENTIALS: Int = 1
        const val AUTH_WEAK_PASSWORD: Int = 2
        const val AUTH_EMAIL_IN_USE: Int = 3
        const val AUTH_USER_DOES_NOT_EXIST: Int = 4
        const val AUTH_FORGOT_PASSWORD_ERROR: Int = 5
    }

    init {
        initMessage(errorId)
    }

    private fun initMessage(errorId: Int) {
        message = when (errorId) {
            AUTH_INVALID_CREDENTIALS -> str(R.string.message_auth_invalid_email_or_pwd)
            AUTH_WEAK_PASSWORD -> str(R.string.message_auth_weak_pwd)
            AUTH_EMAIL_IN_USE -> str(R.string.message_auth_email_in_use)
            AUTH_USER_DOES_NOT_EXIST -> str(R.string.message_auth_user_does_not_exist)
            AUTH_FORGOT_PASSWORD_ERROR -> str(R.string.message_forgot_password_error)

            else -> super.message
        }
    }
}