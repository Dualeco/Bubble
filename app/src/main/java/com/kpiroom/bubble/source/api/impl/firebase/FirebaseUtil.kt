package com.kpiroom.bubble.source.api.impl.firebase

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseUtil(val firebaseDb: FirebaseDatabase) {

    companion object {
        private const val TAG = "FirebaseUtil"
    }

    suspend fun <T : Any> write(
            ref: String,
            value: T
    ): Unit = suspendCancellableCoroutine { continuation ->
        firebaseDb.getReference(ref).setValue(value)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
                .addOnCanceledListener { continuation.cancel() }
    }

    suspend fun <T : Any> read(
            path: String,
            type: Class<T>
    ): T = suspendCancellableCoroutine { continuation ->
        val ref = firebaseDb.getReference(path)
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Read error on cancelled [$path]: $error")
                val exception = when (error.toException()) {
                    is FirebaseException -> error.toException()
                    else -> Exception("The Firebase call was canceled")
                }
                continuation.resumeWithException(exception)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val data: T = snapshot.getValue(type) ?: throw Exception("Empty field")
                    continuation.resume(data)
                } catch (exception: Exception) {
                    Log.d(TAG, "Read error [$ref]: $exception")
                    continuation.resumeWithException(exception)
                }
            }
        }
        continuation.invokeOnCancellation {
            Log.d(TAG, "Canceled $ref")
            ref.removeEventListener(listener)
        }
        ref.addListenerForSingleValueEvent(listener)
    }
}