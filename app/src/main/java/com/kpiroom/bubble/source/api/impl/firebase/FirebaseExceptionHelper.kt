package com.kpiroom.bubble.source.api.impl.firebase

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException


class FirebaseExceptionHelper {
    fun interpretException(exception: Exception): String? {
        return when (exception) {
            is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
            is FirebaseAuthWeakPasswordException -> "Password should be at least 6 characters long"
            is FirebaseAuthUserCollisionException -> "This email address is already in use"
            is FirebaseAuthInvalidUserException -> "User with such email does not exist"
            else -> exception.message
        }

    }
}