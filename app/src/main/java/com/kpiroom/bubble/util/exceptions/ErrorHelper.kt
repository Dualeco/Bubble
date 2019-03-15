package com.kpiroom.bubble.util.exceptions

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.kpiroom.bubble.ui.login.LoginLogic
import com.kpiroom.bubble.util.exceptions.CoreException.Companion.AUTH_EMAIL_IN_USE
import com.kpiroom.bubble.util.exceptions.CoreException.Companion.AUTH_INVALID_CREDENTIALS
import com.kpiroom.bubble.util.exceptions.CoreException.Companion.AUTH_USER_DOES_NOT_EXIST
import com.kpiroom.bubble.util.exceptions.CoreException.Companion.AUTH_WEAK_PASSWORD
import com.kpiroom.bubble.util.exceptions.CoreException.Companion.DEFAULT
import com.kpiroom.bubble.util.exceptions.CoreException.Companion.NETWORK_ERROR

object ErrorHelper {
    fun resolve(throwable: Throwable): CoreException = CoreException(
        when (throwable) {
            is FirebaseAuthWeakPasswordException -> AUTH_WEAK_PASSWORD
            is FirebaseAuthInvalidCredentialsException -> AUTH_INVALID_CREDENTIALS
            is FirebaseAuthUserCollisionException -> AUTH_EMAIL_IN_USE
            is FirebaseAuthInvalidUserException -> AUTH_USER_DOES_NOT_EXIST
            is FirebaseNetworkException -> NETWORK_ERROR
            else -> DEFAULT
        }
    )
}