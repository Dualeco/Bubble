package com.kpiroom.bubble.source.api.impl.firebase

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseStrorageUtil(val storage: FirebaseStorage) {
    companion object {
        private const val TAG = "FirebaseStorageUtil"
    }

    suspend fun uploadFile(
        dirRef: String,
        uri: Uri,
        name: String? = null
    ): String = suspendCancellableCoroutine { continuation ->
        val sourceName = uri.lastPathSegment as String

        val fileName = name?.let {
            "$name.jpg"
        } ?: sourceName

        val fileRef = storage.getReference(dirRef).child(fileName)
        fileRef.putFile(uri)
            .addOnSuccessListener {
                Log.d(TAG, "File uploaded successfully: $uri")
                continuation.resume(fileName)
            }
            .addOnFailureListener {
                Log.d(TAG, "File upload failed: $uri")
                continuation.resumeWithException(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "File upload cancelled: $uri")
                continuation.cancel()
            }
    }

    suspend fun downloadFile(dirRef: String, destination: File): Unit = suspendCancellableCoroutine { continuation ->
        val fileRef = storage.getReference(dirRef)
        fileRef.getFile(destination)
            .addOnSuccessListener {
                Log.d(TAG, "File downloaded successfully: $dirRef ${destination.toUri()}")
                continuation.resume(Unit)
            }
            .addOnFailureListener {
                Log.d(TAG, "File download failed: $dirRef ${destination.toUri()}")
                continuation.resumeWithException(it)
            }
            .addOnCanceledListener {
                Log.d(TAG, "File download cancelled: $destination")
                continuation.cancel()
            }
    }
}