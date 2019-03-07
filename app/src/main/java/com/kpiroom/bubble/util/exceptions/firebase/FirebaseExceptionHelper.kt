package com.kpiroom.bubble.util.exceptions.firebase

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.kpiroom.bubble.R
import com.kpiroom.bubble.util.constants.str

object FirebaseExceptionHelper {
    fun interpretException(exception: Throwable) = when (exception) {
        is FirebaseAuthInvalidCredentialsException -> str(R.string.message_auth_invalid_email_or_pwd)
        is FirebaseAuthWeakPasswordException -> str(R.string.message_auth_weak_pwd)
        is FirebaseAuthUserCollisionException -> str(R.string.message_auth_email_in_use)
        is FirebaseAuthInvalidUserException -> str(R.string.message_auth_user_does_not_exist)
        else -> exception.message
    }
}