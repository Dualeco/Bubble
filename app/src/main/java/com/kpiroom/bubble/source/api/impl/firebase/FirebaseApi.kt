package com.kpiroom.bubble.source.api.impl.firebase

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.ApiInterface
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.COMICS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.PROFILE_PHOTOS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.PROFILE_WALLPAPERS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.USER_KEYS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.util.async.AsyncBag
import com.kpiroom.bubble.util.collectionsAsync.mapAsync
import com.kpiroom.bubble.util.files.getCurrentProfileImageName
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.PREVIEWS
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.THUMBNAILS
import com.kpiroom.bubble.source.api.impl.firebase.livedata.FirebaseListLiveData
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

    override suspend fun getUserUuidList(): List<String> = dbUtil.getUserUuidList()

    override suspend fun getUsername(uuid: String): String? = dbUtil.getUsername(uuid)

    override suspend fun changeUsername(uuid: String, username: String) = dbUtil.changeUsername(uuid, username)

    override suspend fun updateUserPrefs(uuid: String) {
        getUserData(uuid)?.let { data ->
            Source.userPrefs.setFromUser(uuid, data)
        }
    }

    override suspend fun getUserData(uuid: String): User? = dbUtil.getUserData(uuid)

    override suspend fun uploadUserData(uuid: String, user: User): Unit = dbUtil.uploadUserData(uuid, user)

    override fun <T : Any> getChildrenLiveData(
        path: String,
        type: Class<T>,
        orderByPath: String
    ): FirebaseListLiveData<T> = dbUtil.getChildrenLiveData(path, type)

    override fun getAllUploadsLiveData(): FirebaseListLiveData<Comic> = dbUtil.getAllUploadsLiveData()

    override fun getUserUploadsLiveData(uuid: String): FirebaseListLiveData<String> =
        dbUtil.getUserUploadsLiveData(uuid)

    //Auth
    override suspend fun signUp(email: String, password: String): String? = authUtil.signUp(email, password)

    override suspend fun signIn(email: String, password: String): String? =
        authUtil.signIn(email, password)

    override suspend fun sendPasswordResetEmail(email: String): Unit = authUtil.sendPasswordResetEmail(email)

    //Storage
    override suspend fun uploadBitmap(
        dirRef: String,
        bitmap: Bitmap,
        name: String
    ): Uri = storageUtil.uploadBitmap(dirRef, bitmap, name)

    override suspend fun uploadUserPhoto(
        uuid: String,
        bitmap: Bitmap
    ): Uri = uploadBitmap(
        PROFILE_PHOTOS,
        bitmap,
        getCurrentProfileImageName(uuid)
    ).also {
        USER_KEYS(uuid).apply {
            dbUtil.write(PHOTO_URL, it.toString())
        }
    }

    override suspend fun uploadUserWallpaper(
        uuid: String,
        bitmap: Bitmap
    ): Uri = uploadBitmap(
        PROFILE_WALLPAPERS,
        bitmap,
        getCurrentProfileImageName(uuid)
    ).also {
        USER_KEYS(uuid).apply {
            dbUtil.write(WALLPAPER_URL, it.toString())
        }
    }

    override suspend fun uploadComic(
        uuid: String,
        uri: Uri
    ): Uri = uploadFile(COMICS, uri, uuid)

    override suspend fun uploadComicThumbnail(
        uuid: String,
        bitmap: Bitmap
    ): Uri = uploadBitmap(THUMBNAILS, bitmap, uuid)

    override suspend fun uploadComicPreview(
        uuid: String,
        bitmap: Bitmap
    ): Uri = uploadBitmap(PREVIEWS, bitmap, uuid)

    override suspend fun downloadComic(
        comicUuid: String,
        destination: File
    ): Unit = downloadFile("$COMICS/$comicUuid", destination)

    override suspend fun uploadFile(dirRef: String, uri: Uri, name: String?): Unit =
        storageUtil.uploadFile(dirRef, uri, name)

    override suspend fun downloadFile(dirRef: String, destination: File): Unit =
        storageUtil.downloadFile(dirRef, destination)

    override suspend fun usernameExists(bag: AsyncBag, username: String): Boolean =
        Source.api.run {
            getUserUuidList().mapAsync(bag) { uuid ->
                getUsername(uuid)
            }.contains(username)
        }

    override suspend fun getComicData(uuid: String): Comic? = dbUtil.getComicData(uuid)

    override suspend fun uploadComicData(uuid: String, comic: Comic) = dbUtil.uploadComicData(uuid, comic)

    fun removeComicThumbnail(uuid: String) = storageUtil.deleteFile("$THUMBNAILS/$uuid")

    fun removeComicFile(uuid: String) = storageUtil.deleteFile("$COMICS/$uuid")

    override fun removeComic(comic: Comic) {
        dbUtil.removeComicData(comic)
        storageUtil.apply {
            removeComicThumbnail(comic.uuid)
            removeComicFile(comic.uuid)
        }
    }
}