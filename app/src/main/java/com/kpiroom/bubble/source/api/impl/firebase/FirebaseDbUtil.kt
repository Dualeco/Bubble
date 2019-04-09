package com.kpiroom.bubble.source.api.impl.firebase

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.COMICS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USERNAMES
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USERS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USER_KEYS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.source.api.impl.firebase.livedata.FirebaseListLiveData
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

    fun remove(path: String) {
        firebaseDb.getReference(path).removeValue()
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
            read("$USERNAMES/$uuid", String::class.java)
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
        write("$USERNAMES/$uuid", user.username)
    }

    suspend fun usernameExists(username: String): Boolean = try {
        readChildren(USERNAMES, String::class.java)
            .contains(username)
    } catch (ex: DbEmptyFieldException) {
        false
    }

    suspend fun changeUsername(uuid: String, username: String) {
        write("$USERNAMES/$uuid", username)
        write(USER_KEYS(uuid).USERNAME, username)
    }

    suspend fun removeComicData(uuid: String) {
        getComicData(uuid)?.run {
            remove("${USER_KEYS(authorId).UPLOADS}/$uuid")
            remove("$COMICS/$uuid")
        }
    }

    suspend fun getComicData(uuid: String): Comic? =
        try {
            read("$COMICS/$uuid", Comic::class.java)
        } catch (ex: DbEmptyFieldException) {
            null
        }

    suspend fun uploadComicData(uuid: String, comic: Comic) {
        write("$COMICS/$uuid", comic)
        write("${USER_KEYS(comic.authorId).UPLOADS}/$uuid", uuid)
    }

    fun <T : Any> getChildrenLiveData(path: String, type: Class<T>): FirebaseListLiveData<T> =
        FirebaseListLiveData(firebaseDb.getReference(path), type)

    fun getUploadsLiveData(uuid: String): FirebaseListLiveData<String> = getChildrenLiveData(
        USER_KEYS(uuid).UPLOADS,
        String::class.java
    )
}