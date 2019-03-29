package com.kpiroom.bubble.source.api.impl.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.ApiInterface
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.PROFILE_PHOTOS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.PROFILE_WALLPAPERS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USER_KEYS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.util.files.deleteProfileWallpaper
import com.kpiroom.bubble.util.files.getCurrentProfileImageName
import com.kpiroom.bubble.util.pref.setFromUser
import java.io.File

class FirebaseApi : ApiInterface {

    private val dbUtil = FirebaseDbUtil(FirebaseDatabase.getInstance().apply {
        setPersistenceEnabled(false)
    })

    private val authUtil = FirebaseAuthUtil(FirebaseAuth.getInstance())
    private val storageUtil = FirebaseStrorageUtil(FirebaseStorage.getInstance())

    //Database
    override suspend fun getServerVersion(): String = dbUtil.read(FirebaseStructure.VERSION, String::class.java)

    override suspend fun setServerVersion(version: String): Unit = dbUtil.write(FirebaseStructure.VERSION, version)

    override suspend fun usernameExists(username: String): Boolean = dbUtil.usernameExists(username)

    override suspend fun getUsername(uuid: String): String? = dbUtil.getUsername(uuid)

    override suspend fun changeUsername(uuid: String, username: String) = dbUtil.changeUsername(uuid, username)

    override suspend fun updateUserPrefs(uuid: String) {
        getUserData(uuid)?.let { data ->
            Source.userPrefs.setFromUser(uuid, data)
        }
    }

    override suspend fun getUserData(uuid: String): User? = dbUtil.getUserData(uuid)

    override suspend fun uploadUserData(uuid: String, user: User): Unit = dbUtil.uploadUserData(uuid, user)

    //Auth
    override suspend fun signUp(email: String, password: String): String? = authUtil.signUp(email, password)

    override suspend fun signIn(email: String, password: String): String? =
        authUtil.signIn(email, password)

    override suspend fun sendPasswordResetEmail(email: String): Unit = authUtil.sendPasswordResetEmail(email)

    //Storage
    override suspend fun uploadFile(
        dirRef: String,
        uri: Uri,
        name: String
    ): Unit = storageUtil.uploadFile(dirRef, uri, name)

    override suspend fun uploadUserPhoto(
        uuid: String,
        uri: Uri
    ): Unit = dbUtil.run {
        Source.userPrefs.apply {
            isPhotoSet = true
            uploadFile(PROFILE_PHOTOS, uri, photoName)

            USER_KEYS(uuid).apply {
                write(PHOTO_NAME, photoName)
                write(PHOTO_SET, true)
            }
        }
    }

    override suspend fun uploadUserWallpaper(
        uuid: String,
        uri: Uri
    ): Unit = dbUtil.run {
        Source.userPrefs.apply {
            isWallpaperSet = true
            uploadFile(PROFILE_WALLPAPERS, uri, wallpaperName)

            USER_KEYS(uuid).apply {
                write(WALLPAPER_NAME, wallpaperName)
                write(WALLPAPER_SET, true)
            }
        }
    }

    override suspend fun downloadFile(dirRef: String, destination: File): Unit =
        storageUtil.downloadFile(dirRef, destination)

    override suspend fun downloadUserPhoto(photoName: String, destination: File): Unit =
        storageUtil.downloadFile("$PROFILE_PHOTOS/$photoName", destination)

    override suspend fun downloadUserWallpaper(wallpaperName: String, destination: File): Unit =
        storageUtil.downloadFile("$PROFILE_WALLPAPERS/$wallpaperName", destination)
}