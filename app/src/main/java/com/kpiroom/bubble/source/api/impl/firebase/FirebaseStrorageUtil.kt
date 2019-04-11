package com.kpiroom.bubble.source.api.impl.firebase

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseStrorageUtil(val storage: FirebaseStorage) {
    companion object {
        private const val TAG = "FirebaseStorageUtil"
    }

    suspend fun uploadBitmap(
        dirRef: String,
        bitmap: Bitmap,
        name: String
    ): Uri = suspendCancellableCoroutine { continuation ->
        val fileRef = storage.getReference(dirRef).child(name)
        val data = ByteArrayOutputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, it)
            it.toByteArray()
        }
        fileRef.putBytes(data).continueWithTask { task ->
            task.run {
                when {
                    !isSuccessful -> {
                        Log.d(TAG, "Bitmap upload failed: $name")
                        continuation.resumeWithException(exception as Throwable)
                    }
                    isCanceled -> {
                        Log.d(TAG, "Bitmap upload cancelled: $name")
                        continuation.cancel()
                    }
                    isSuccessful -> {
                        Log.d(TAG, "Bitmap upload succesful: $name")
                    }
                }
                fileRef.downloadUrl
            }
        }.apply {
            addOnSuccessListener {
                Log.d(TAG, "Url retrieved successfully for bitmap $name: $it")
                continuation.resume(it)
            }
            addOnFailureListener {
                Log.d(TAG, "Url retrieval failed for bitmap $name")
                continuation.resumeWithException(it)
            }
            addOnCanceledListener {
                Log.d(TAG, "Url retrieval cancelled for bitmap $name")
                continuation.cancel()
            }
        }
    }

    suspend fun uploadFile(
        dirRef: String,
        uri: Uri,
        name: String? = null
    ): Unit = suspendCancellableCoroutine { continuation ->
        val sourceName = uri.lastPathSegment as String

        val fileName = name ?: sourceName

        val fileRef = storage.getReference(dirRef).child(fileName)
        fileRef.putFile(uri)
            .addOnSuccessListener {
                Log.d(TAG, "File uploaded successfully: $uri")
                continuation.resume(Unit)
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

    suspend fun downloadFile(
        dirRef: String,
        destination: File
    ): Unit = suspendCancellableCoroutine { continuation ->
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