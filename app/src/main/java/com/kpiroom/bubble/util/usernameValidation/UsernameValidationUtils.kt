package com.kpiroom.bubble.util.usernameValidation

import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.exceptions.core.CoreException
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.NONE
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.USERNAME_EMPTY
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.USERNAME_EXISTS
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.USERNAME_INVALID_FORMAT
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.USERNAME_TOO_LONG
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.USERNAME_TOO_SHORT

suspend fun validateUsername(username: String?) {
    username.let {
        val usernameRegex = str(R.string.username_regex).toRegex()
        CoreException(
            when {
                it.isNullOrBlank() -> USERNAME_EMPTY
                it.length < 4 -> USERNAME_TOO_SHORT
                it.length > 16 -> USERNAME_TOO_LONG
                !it.matches(usernameRegex) -> USERNAME_INVALID_FORMAT
                Source.api.usernameExists(it) -> USERNAME_EXISTS

                else -> NONE
            }
        ).run {
            if (errorId != NONE) throw this@run
        }
    }
}