package com.kpiroom.bubble.source.api.impl.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthUtil(val auth: FirebaseAuth) {
    companion object {
        private const val TAG = "FirebaseAuthUtil"
    }

    suspend fun signUp(
        email: String,
        password: String
    ): String? = suspendCancellableCoroutine { continuation ->
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val id = result?.user?.uid
                Log.d(TAG, "Sign up successful for $email, id: $id")
                continuation.resume(id)
            }
            .addOnFailureListener {
                Log.d(TAG, "Sign up unsuccessful for $email, exception: $it")
                continuation.resumeWithException(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "Sign up cancelled for $email")
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
                Log.d(TAG, "Sign in successful for $email, id: $id")
                continuation.resume(id)
            }
            .addOnFailureListener {
                Log.d(TAG, "Sign in unsuccessful for $email, exception: $it")
                continuation.resumeWithException(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "Sign in cancelled for $email")
                continuation.cancel()
            }
    }

    suspend fun sendPasswordResetEmail(
        email: String
    ): Unit = suspendCancellableCoroutine { continuation ->
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "Email sent successfully to email $email")
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                Log.d(TAG, "Email sending failed $email, exception: $it")
                continuation.resumeWithException(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "Email sending canceled $email")
                continuation.cancel()
            }
    }

    suspend fun emailExists(
        email: String
    ): Boolean = suspendCancellableCoroutine { continuation ->
        Log.d(TAG, "Checking")
        auth.fetchSignInMethodsForEmail(email)
            .addOnSuccessListener {
                Log.d(TAG, "Check successful")
                val found = !(it.signInMethods?.isEmpty() ?: true)
                continuation.resume(found)
            }
            .addOnFailureListener {
                Log.d(TAG, "Check failed")
                continuation.resumeWithException(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "Check cancelled")
                continuation.cancel()
            }
    }

}