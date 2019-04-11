package com.kpiroom.bubble.source.api

import android.graphics.Bitmap
import android.net.Uri
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.util.async.AsyncBag
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.source.api.impl.firebase.livedata.FirebaseListLiveData
import java.io.File

interface ApiInterface {

    //Database
    suspend fun getServerVersion(): String

    suspend fun setServerVersion(version: String)

    suspend fun getUserUuidList(): List<String>

    suspend fun getUsername(uuid: String): String?

    suspend fun changeUsername(uuid: String, username: String)

    suspend fun updateUserPrefs(uuid: String)

    suspend fun getUserData(uuid: String): User?

    suspend fun uploadUserData(uuid: String, user: User)

    suspend fun getComicData(uuid: String): Comic?

    suspend fun uploadComicData(uuid: String, comic: Comic)

    //Auth
    suspend fun signUp(email: String, password: String): String?

    suspend fun signIn(email: String, password: String): String?

    suspend fun sendPasswordResetEmail(email: String)

    //Storage
    suspend fun uploadBitmap(dirRef: String, bitmap: Bitmap, name: String): Uri

    suspend fun uploadUserPhoto(uuid: String, bitmap: Bitmap): Uri

    suspend fun uploadUserWallpaper(uuid: String, bitmap: Bitmap): Uri

    suspend fun uploadFile(dirRef: String, uri: Uri, name: String? = null)

    suspend fun uploadComic(uuid: String, uri: Uri): Uri

    suspend fun downloadComic(comicUuid: String, destination: File)

    suspend fun downloadFile(dirRef: String, destination: File)

    suspend fun usernameExists(bag: AsyncBag, username: String): Boolean

    suspend fun downloadUserPhoto(photoName: String, destination: File)

    suspend fun downloadUserWallpaper(wallpaperName: String, destination: File)

    suspend fun uploadComicThumbnail(uuid: String, bitmap: Bitmap): Uri

    suspend fun uploadComicPreview(uuid: String, bitmap: Bitmap): Uri

    fun getUserUploadsLiveData(uuid: String): FirebaseListLiveData<String>

    fun <T : Any> getChildrenLiveData(path: String, type: Class<T>, orderByPath: String): FirebaseListLiveData<T>

    fun removeComic(comic: Comic)
    fun getAllUploadsLiveData(): FirebaseListLiveData<Comic>
}