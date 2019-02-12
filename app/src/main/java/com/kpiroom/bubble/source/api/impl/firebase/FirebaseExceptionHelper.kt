package com.kpiroom.bubble.source.api.impl.firebase

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.kpiroom.bubble.util.constant.str
import com.kpiroom.bubble.R

object FirebaseExceptionHelper {
    fun interpretException(exception: Throwable): String? {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException -> str(R.string.message_firebase_invalid_email_or_pwd)
            is FirebaseAuthWeakPasswordException -> str(R.string.message_firebase_weak_pwd)
            is FirebaseAuthUserCollisionException -> str(R.string.message_firebase_email_already_in_use)
            is FirebaseAuthInvalidUserException -> str(R.string.message_firebase_user_does_not_exist)
            else -> exception.message
        }

    }
}