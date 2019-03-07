package com.kpiroom.bubble.util.exceptions

import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.kpiroom.bubble.util.exceptions.BubbleException.Companion.AUTH_INVALID_CREDENTIALS
import com.kpiroom.bubble.util.exceptions.BubbleException.Companion.AUTH_USER_DOES_NOT_EXIST
import com.kpiroom.bubble.util.exceptions.BubbleException.Companion.AUTH_EMAIL_IN_USE
import com.kpiroom.bubble.util.exceptions.BubbleException.Companion.AUTH_WEAK_PASSWORD
import com.kpiroom.bubble.util.exceptions.BubbleException.Companion.DEFAULT
import java.lang.Exception

object ErrorHelper {
    fun resolve(throwable: Throwable, errorId: Int = DEFAULT): BubbleException = BubbleException(
        when (throwable) {
            is FirebaseAuthInvalidCredentialsException -> AUTH_INVALID_CREDENTIALS
            is FirebaseAuthWeakPasswordException -> AUTH_WEAK_PASSWORD
            is FirebaseAuthUserCollisionException -> AUTH_EMAIL_IN_USE
            is FirebaseAuthInvalidUserException -> AUTH_USER_DOES_NOT_EXIST
            else -> errorId
        }
    )
}