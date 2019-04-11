package com.kpiroom.bubble.source.api.impl.firebase

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USER_UUIDS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USERS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USER_KEYS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.util.exceptions.db.DbCancelledException
import com.kpiroom.bubble.util.exceptions.db.DbEmptyFieldException
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
        type: Class<T>
    ): T =
        suspendCancellableCoroutine { continuation ->
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

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val data: T = snapshot.getValue(type) ?: throw DbEmptyFieldException()
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

    suspend fun <T : Any?> readChildren(
        path: String,
        childType: Class<T>
    ): List<T> =
        suspendCancellableCoroutine { continuation ->
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

                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val children = snapshot.children.map {
                            it.getValue(childType) ?: throw DbEmptyFieldException()
                        }

                        continuation.resume(children)
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

    suspend fun getUsername(uuid: String): String? =
        try {
            read(USER_KEYS(uuid).USERNAME, String::class.java)
        } catch (ex: DbEmptyFieldException) {
            null
        }

    suspend fun getUserData(uuid: String): User? =
        try {
            read("$USERS/$uuid", User::class.java)
        } catch (ex: DbEmptyFieldException) {
            null
        }

    suspend fun uploadUserData(uuid: String, user: User) {
        write("$USERS/$uuid", user)
        write("$USER_UUIDS/$uuid", uuid)
    }

    suspend fun getUserUuidList(): List<String> = try {
        readChildren(USER_UUIDS, String::class.java)
    } catch (ex: DbEmptyFieldException) {
        listOf()
    }

    suspend fun changeUsername(uuid: String, username: String) = write(USER_KEYS(uuid).USERNAME, username)
}