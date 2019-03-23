package com.kpiroom.bubble.source.api.impl.firebase

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.ApiInterface
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USER_KEYS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.util.date.timeStamp
import com.kpiroom.bubble.util.pref.setFromUser
import java.io.File

class FirebaseApi : ApiInterface {

    private val dbUtil = FirebaseDbUtil(FirebaseDatabase.getInstance().apply {
        setPersistenceEnabled(false)
    })

    private val authUtil = FirebaseAuthUtil(FirebaseAuth.getInstance())
    private val storageUtil = FirebaseStrorageUtil(FirebaseStorage.getInstance())

    //Database
    override suspend fun getServerVersion(): String = dbUtil.read(FirebaseStructure.VERSION)

    override suspend fun setServerVersion(version: String): Unit = dbUtil.write(FirebaseStructure.VERSION, version)

    override suspend fun usernameExists(username: String): Boolean = dbUtil.usernameExists(username)

    override suspend fun fetchUserPrefs(uuid: String) {
        getUserData(uuid)?.let { data ->
            Source.userPrefs.setFromUser(uuid, data)
        }
    }

    override suspend fun getUserData(uuid: String?): User? = dbUtil.getUserData(uuid)

    override suspend fun uploadUserData(uuid: String?, user: User): Unit = dbUtil.uploadUserData(uuid, user)

    //Auth
    override suspend fun signUp(email: String, password: String): String? = authUtil.signUp(email, password)

    override suspend fun signIn(email: String, password: String): String? =
        authUtil.signIn(email, password)?.also { uuid ->
            fetchUserPrefs(uuid)
        }

    override suspend fun sendPasswordResetEmail(email: String): Unit = authUtil.sendPasswordResetEmail(email)

    //Storage
    override suspend fun uploadFile(
        dirRef: String,
        uri: Uri,
        name: String?
    ): String = storageUtil.uploadFile(dirRef, uri, name)

    override suspend fun uploadUserPhoto(
        uuid: String,
        uri: Uri
    ): String = uploadFile(FirebaseStructure.PROFILE_PHOTOS, uri, uuid).also {
        Source.userPrefs.isPhotoSet = true
        dbUtil.write(USER_KEYS(uuid).PHOTO_SET, true)
    }

    override suspend fun uploadUserWallpaper(
        uuid: String,
        uri: Uri
    ): String = uploadFile(FirebaseStructure.PROFILE_WALLPAPERS, uri, uuid).also {
        Source.userPrefs.isPhotoSet = true
        dbUtil.write(USER_KEYS(uuid).WALLPAPER_SET, true)
    }

    override suspend fun downloadFile(dirRef: String, destination: File): Unit =
        storageUtil.downloadFile(dirRef, destination)

    override suspend fun downloadUserPhoto(uuid: String?, destination: File): Unit =
        storageUtil.downloadFile("${FirebaseStructure.PROFILE_PHOTOS}/$uuid.jpg", destination)

    override suspend fun downloadUserWallpaper(uuid: String?, destination: File): Unit =
        storageUtil.downloadFile("${FirebaseStructure.PROFILE_WALLPAPERS}/$uuid.jpg", destination)
}
