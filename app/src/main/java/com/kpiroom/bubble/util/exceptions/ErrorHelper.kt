package com.kpiroom.bubble.util.exceptions

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.kpiroom.bubble.util.exceptions.core.CoreException
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.AUTH_EMAIL_IN_USE
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.AUTH_INVALID_CREDENTIALS
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.AUTH_USER_DOES_NOT_EXIST
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.AUTH_WEAK_PASSWORD
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.DB_CALL_CANCELLED
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.DB_EMPTY_FIELD
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.DEFAULT
import com.kpiroom.bubble.util.exceptions.core.CoreException.Companion.NETWORK_ERROR
import com.kpiroom.bubble.util.exceptions.db.DbCancelledException
import com.kpiroom.bubble.util.exceptions.db.DbConnectionException
import com.kpiroom.bubble.util.exceptions.db.DbEmptyFieldException

object ErrorHelper {
    private const val TAG = "ErrorHelper"

    fun resolve(throwable: Throwable): CoreException =
        CoreException(
            when (throwable) {
                is CoreException -> throwable.errorId
                is FirebaseAuthWeakPasswordException -> AUTH_WEAK_PASSWORD
                is FirebaseAuthInvalidCredentialsException -> AUTH_INVALID_CREDENTIALS
                is FirebaseAuthUserCollisionException -> AUTH_EMAIL_IN_USE
                is FirebaseAuthInvalidUserException -> AUTH_USER_DOES_NOT_EXIST
                is FirebaseNetworkException -> NETWORK_ERROR
                is DbConnectionException -> NETWORK_ERROR
                is DbEmptyFieldException -> DB_EMPTY_FIELD
                is DbCancelledException -> DB_CALL_CANCELLED
                else -> DEFAULT
            },
            throwable
        ).also { exception ->
            Log.e(TAG, "$exception\n")
        }
}