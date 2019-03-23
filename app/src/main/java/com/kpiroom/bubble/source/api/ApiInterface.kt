package com.kpiroom.bubble.source.api

import android.net.Uri
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import java.io.File

interface ApiInterface {

    //Database
    suspend fun getServerVersion(): String

    suspend fun setServerVersion(version: String)

    suspend fun usernameExists(username: String): Boolean

    suspend fun fetchUserPrefs(uuid: String)

    suspend fun getUserData(uuid: String?): User?

    suspend fun uploadUserData(uuid: String?, user: User)

    //Auth
    suspend fun signUp(email: String, password: String): String?

    suspend fun signIn(email: String, password: String): String?

    suspend fun sendPasswordResetEmail(email: String)

    //Storage
    suspend fun uploadFile(
        dirRef: String,
        uri: Uri,
        name: String?
    ): String

    suspend fun uploadUserPhoto(
        uuid: String,
        uri: Uri
    ): String

    suspend fun uploadUserWallpaper(
        uuid: String,
        uri: Uri
    ): String

    suspend fun downloadFile(dirRef: String, destination: File)

    suspend fun downloadUserPhoto(uuid: String?, destination: File)

    suspend fun downloadUserWallpaper(uuid: String?, destination: File)
}