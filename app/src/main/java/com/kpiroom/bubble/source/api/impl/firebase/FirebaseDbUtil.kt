package com.kpiroom.bubble.source.api.impl.firebase

import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.COMICS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USER_UUIDS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USERS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.COMIC_KEYS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USER_KEYS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.source.api.impl.firebase.livedata.FirebaseListLiveData
import com.kpiroom.bubble.source.api.impl.firebase.livedata.FirebaseMapLiveData
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


    suspend fun <T : Any?> readMap(
        path: String,
        childType: Class<T>
    ): Map<String, T> =
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
                        val children = snapshot.children.associate {
                            Pair(it.key?: throw DbEmptyFieldException(), it.getValue(childType)?: throw DbEmptyFieldException())
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

    suspend fun changeUsername(uuid: String, username: String) =
        write(USER_KEYS(uuid).USERNAME, username)

    fun removeComicData(comic: Comic) = comic.run {
        remove("${USER_KEYS(authorId).UPLOADS}/$id")
        remove("$COMICS/$id")
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

    fun <T : Any> getMapLiveData(path: String, type: Class<T>): FirebaseMapLiveData<T> =
        FirebaseMapLiveData(firebaseDb.getReference(path), type)

    fun getUserComicsIds(uuid: String): FirebaseListLiveData<String> = getChildrenLiveData(
        USER_KEYS(uuid).UPLOADS,
        String::class.java
    )

    fun getCommonComicsIds(): FirebaseListLiveData<FirebaseStructure.Comic> = getChildrenLiveData(
        COMICS,
        Comic::class.java
    )

    fun getUserFavorites(uuid: String): FirebaseMapLiveData<Long> = getMapLiveData(
        USER_KEYS(uuid).FAVORITES,
        Long::class.java
    )

    suspend fun userIsSubscribedTo(userId: String, authorId: String): Boolean = try {
        read("${USER_KEYS(userId).SUBSCRIPTIONS}/$authorId", Long::class.java)
        true
    } catch (e: DbEmptyFieldException) {
        false
    }

    suspend fun subscribeTo(userId: String, authorId: String){
        write("${USER_KEYS(userId).SUBSCRIPTIONS}/$authorId", System.currentTimeMillis())
        write("${USER_KEYS(authorId).SUBSCRIBERS}/$userId", userId)
    }

    fun unsubscribeFrom(userId: String, authorId: String) {
        remove("${USER_KEYS(userId).SUBSCRIPTIONS}/$authorId")
        remove("${USER_KEYS(authorId).SUBSCRIBERS}/$userId")
    }

    suspend fun getUserSubscriptions(uuid: String): Map<String, Long> = readMap(
        USER_KEYS(uuid).SUBSCRIPTIONS,
        Long::class.java
    )

    suspend fun addToFavorites(userId: String, comicId: String) {
        write("${USER_KEYS(userId).FAVORITES}/$comicId", System.currentTimeMillis())
        write("${COMIC_KEYS(comicId).STARRED}/$userId", userId)
    }

    fun removeFromFavorites(userId: String, comicId: String) {
        remove("${USER_KEYS(userId).FAVORITES}/$comicId")
        remove("${COMIC_KEYS(comicId).STARRED}/$userId")
    }

    suspend fun getStarsForComic(comicId: String): Int = readChildren(
        COMIC_KEYS(comicId).STARRED,
        String::class.java
    ).size

    suspend fun isComicStarred(userId: String, comicId: String): Boolean = readChildren(
        COMIC_KEYS(comicId).STARRED,
        String::class.java
    ).contains(userId)
}