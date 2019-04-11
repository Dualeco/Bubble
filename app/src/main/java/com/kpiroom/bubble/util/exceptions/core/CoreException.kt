package com.kpiroom.bubble.util.exceptions.core

import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.constants.str
import java.io.PrintWriter
import java.io.StringWriter

data class CoreException(
    val errorId: Int = DEFAULT,
    override val cause: Throwable? = null,
    override val message: String = initMessage(errorId)
) : Exception() {

    companion object {
        const val NONE: Int = -1
        const val DEFAULT: Int = 0
        const val AUTH_INVALID_CREDENTIALS: Int = 1
        const val AUTH_WEAK_PASSWORD: Int = 2
        const val AUTH_EMAIL_IN_USE: Int = 3
        const val AUTH_USER_DOES_NOT_EXIST: Int = 4
        const val NETWORK_ERROR: Int = 5
        const val DB_EMPTY_FIELD: Int = 6
        const val DB_CALL_CANCELLED: Int = 7
        const val USERNAME_EMPTY: Int = 8
        const val USERNAME_TOO_SHORT: Int = 9
        const val USERNAME_TOO_LONG: Int = 10
        const val USERNAME_INVALID_FORMAT: Int = 11
        const val USERNAME_EXISTS: Int = 12

        private fun initMessage(errorId: Int) = when (errorId) {
            AUTH_INVALID_CREDENTIALS -> str(R.string.message_auth_invalid_email_or_pwd)
            AUTH_WEAK_PASSWORD -> str(R.string.message_auth_weak_pwd)
            AUTH_EMAIL_IN_USE -> str(R.string.message_auth_email_in_use)
            AUTH_USER_DOES_NOT_EXIST -> str(R.string.message_auth_user_does_not_exist)
            NETWORK_ERROR -> str(R.string.common_no_internet)
            DB_EMPTY_FIELD -> str(R.string.db_field_empty)
            DB_CALL_CANCELLED -> str(R.string.db_call_cancelled)
            USERNAME_EMPTY -> str(R.string.setup_username_cannot_be_empty)
            USERNAME_TOO_SHORT -> str(R.string.setup_username_too_short)
            USERNAME_TOO_LONG -> str(R.string.setup_username_too_long)
            USERNAME_INVALID_FORMAT -> str(R.string.setup_username_invalid)
            USERNAME_EXISTS -> str(R.string.username_exists)

            else -> str(R.string.common_error_message)
        }
    }

    override fun toString(): String = StringWriter().apply {
        cause?.printStackTrace(PrintWriter(this)) ?: message
    }.toString()
}