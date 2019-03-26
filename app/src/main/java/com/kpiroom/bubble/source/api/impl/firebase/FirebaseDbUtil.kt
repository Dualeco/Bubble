package com.kpiroom.bubble.source.api.impl.firebase

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USERNAMES
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USERS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.util.exceptions.core.db.DbCancelledException
import com.kpiroom.bubble.util.exceptions.core.db.DbEmptyFieldException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseDbUtil(val firebaseDb: FirebaseDatabase) {

    companion object {
        private const val TAG = "FirebaseDbUtil"
    }

    suspend fun <T : Any?> write(
        ref: String,
        value: T
    ): Unit = suspendCancellableCoroutine { continuation ->
        firebaseDb.getReference(ref).setValue(value)
            .addOnSuccessListener { continuation.resume(Unit) }
            .addOnFailureListener { continuation.resumeWithException(it) }
            .addOnCanceledListener { continuation.cancel() }
    }

    suspend fun <T : Any?> read(
        path: String,
        customType: Class<T>? = null
    ): T = suspendCancellableCoroutine { continuation ->
        val ref = firebaseDb.getReference(path)
        val listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Read error on cancelled [$path]: $error")
                val exception = when (error.toException()) {
                    is FirebaseException -> error.toException()
                    else -> DbCancelledException()
                }
                continuation.resumeWithException(exception)
            }

            override fun onDataChange(snapshot: DataSnapshot) =
                try {
                    val data: T = customType?.let {
                        snapshot.getValue(it)
                    } ?: run {
                        snapshot.value as? T
                    } ?: throw DbEmptyFieldException()

                    continuation.resume(data)
                } catch (exception: Exception) {
                    Log.d(TAG, "Read error [$ref]: $exception")
                    continuation.resumeWithException(exception)
                }
        }

        continuation.invokeOnCancellation {
            Log.d(TAG, "Canceled $ref")
            ref.removeEventListener(listener)
        }
        ref.addListenerForSingleValueEvent(listener)
    }

    suspend fun getUsername(uuid: String): String? =
        try {
            read("$USERNAMES/$uuid")
        } catch (ex: DbEmptyFieldException) {
            null
        }

    suspend fun getUserData(uuid: String): User? =
        try {
            read("$USERS/$uuid", User::class.java)
        } catch (ex: DbEmptyFieldException) {
            null
        }


    suspend fun uploadUserData(uuid: String?, user: User) {
        uuid?.let { id ->
            write("$USERS/$id", user)
            write("$USERNAMES/$id", user.username)
        }
    }

    suspend fun usernameExists(username: String): Boolean = try {
        val m = read<Map<String, String>>(USERNAMES)
        Log.d(TAG, "Empty")
        m.containsValue(username)
    } catch (ex: DbEmptyFieldException) {
        Log.d(TAG, "Ex")
        false
    }
}