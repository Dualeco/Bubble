package com.kpiroom.bubble.source.api.impl.firebase

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.database.*
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.IS_CONNECTED
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USERNAMES
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USERS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.exceptions.core.db.DbCancelledException
import com.kpiroom.bubble.util.exceptions.core.db.DbEmptyFieldException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseDbUtil(val firebaseDb: FirebaseDatabase) {

    companion object {
        private const val TAG = "FirebaseDbUtil"
    }

    private suspend fun isConnected(): Boolean = read(IS_CONNECTED)

    suspend fun <T : Any?> write(
        ref: String,
        value: T
    ): Unit = suspendCancellableCoroutine { continuation ->
        firebaseDb.getReference(ref).setValue(value)
            .addOnSuccessListener { continuation.resume(Unit) }
            .addOnFailureListener { continuation.resumeWithException(it) }
            .addOnCanceledListener { continuation.cancel() }
    }

    suspend fun <T : Any?> read(path: String): T = suspendCancellableCoroutine { continuation ->
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
                    val data: T = snapshot.value as? T ?: throw DbEmptyFieldException()
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

    private suspend fun <T : Any?> readIfConnected(path: String): T =
        if (isConnected())
            read(path)
        else
            throw FirebaseNetworkException(str(R.string.db_no_connection))

    suspend fun setUpAccount(): Unit = Source.userPrefs.run {
        uuid?.also { id ->
            write("$USERS/$id", User(username, joinedDateString))
            write("$USERNAMES/$id", username)
        }
    }

    suspend fun usernameExists(username: String): Boolean = try {
        readIfConnected<Map<String, String>>(USERNAMES).containsValue(username)
    } catch (ex: DbEmptyFieldException) {
        false
    }
}