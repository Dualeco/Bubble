package com.kpiroom.bubble.source.api.impl.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthUtil(val auth: FirebaseAuth) {
    companion object {
        const val TAG = "FirebaseAuthUtil"
    }

    suspend fun doesEmailExist(
        email: String
    ): Boolean = suspendCancellableCoroutine { continuation ->
        Log.d(TAG, "Checking")
        auth.fetchSignInMethodsForEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "Check successfull")
                val found = !(it.signInMethods?.isEmpty() ?: true)
                continuation.resume(found)
            }
            .addOnFailureListener {
                Log.d(TAG, "Check failed")
                continuation.cancel(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "Check cancelled")
                continuation.cancel()
            }
    }


    suspend fun signUp(
        email: String,
        password: String
    ): String? = suspendCancellableCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val id = result?.user?.uid
                Log.d(TAG, "Sign up successful")
                continuation.resume(id)
            }
            .addOnFailureListener {
                Log.d(TAG, "Sign up unsuccessful, $it")
                continuation.resumeWithException(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "Sign up cancelled")
                continuation.cancel()
            }
    }

    suspend fun signIn(
        email: String,
        password: String
    ): String? = suspendCancellableCoroutine { continuation ->
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val id = result?.user?.uid
                Log.d(TAG, "Sign in successful $id")
                continuation.resume(id)
            }
            .addOnFailureListener {
                Log.d(TAG, "Sign in failed, $it")
                continuation.resumeWithException(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "Sign in cancelled")
                continuation.cancel()
            }
    }

    suspend fun sendPasswordResetEmail(
        email: String
    ): Unit = suspendCancellableCoroutine { continuation ->
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "Email sent successfully")
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                Log.d(TAG, "Email sending failed")
                continuation.resumeWithException(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "Email sending canceled")
                continuation.cancel()
            }
    }
}