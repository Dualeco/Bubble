package com.kpiroom.bubble.ui.main.fragments.profile

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.ui.progress.ProgressFragmentLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.bitmap.extractBitmapFrom
import com.kpiroom.bubble.util.collectionsAsync.mapAsync
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.collectionsAsync.mapAsync
import com.kpiroom.bubble.util.imageUpload.showImageSelectionAlert
import com.kpiroom.bubble.util.livedata.progressState.*
import com.kpiroom.bubble.util.livedata.setDefault
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.usernameValidation.validateUsernameAsync
import com.kpiroom.bubble.util.recyclerview.tabs.FavoritesAdapter
import com.kpiroom.bubble.util.recyclerview.tabs.SubscriptionAdapter
import com.kpiroom.bubble.util.recyclerview.tabs.UploadsAdapter
import kotlinx.coroutines.Dispatchers

class ProfileLogic : ProgressFragmentLogic() {

    override val progress = MutableLiveData<ProgressState>()

    val uploadsLiveData = Source.run {
        api.getUserUploadsLiveData(userPrefs.uuid)
    }
    val scrollFlagsOn = SCROLL_FLAG_SCROLL or SCROLL_FLAG_EXIT_UNTIL_COLLAPSED

    val scrollFlagsOff = 0

    val restoreFocus = MutableLiveData<Boolean>()

    val scrollFlags = MutableLiveData<Int>().setDefault(scrollFlagsOn)

    val username = MutableLiveData<String>().setDefault(Source.userPrefs.username)

    val isTitleEditable = MutableLiveData<Boolean>()

    val joinedOn = Source.userPrefs.joinedDate

    val useCameraForPhoto = MutableLiveData<Boolean>()

    var photoUri = MutableLiveData<Uri>().apply {
        Source.userPrefs.photoDownloadUri.let {
            if (!it.toString().isBlank())
                value = it
        }
    }

    val photoChangeRequested = MutableLiveData<Boolean>()

    var wallpaperUri = MutableLiveData<Uri>().apply {
        Source.userPrefs.wallpaperDownloadUri.let {
            if (!it.toString().isBlank())
                value = it
        }
    }

    val channelsAdapter: SubscriptionAdapter = SubscriptionAdapter(
        listOf(),
        ::onChannelClicked,
        ::onChannelFollowed
    )

    val favoritesAdapter: FavoritesAdapter = FavoritesAdapter(
        listOf(),
        ::onFavoriteClicked
    )

    val uploadsAdapter: UploadsAdapter = UploadsAdapter(
        listOf(),
        ::onUploadClicked,
        ::onUploadDeleted
    )

    val wallpaperChangeRequested = MutableLiveData<Boolean>()

    val optionWindowClicked = MutableLiveData<Boolean>()
    val loggedOut = MutableLiveData<Boolean>()

    val channelList = MutableLiveData<MutableList<User>>().setDefault(
        mutableListOf()
    )
    val favoriteList = MutableLiveData<MutableList<Comic>>().setDefault(
        mutableListOf(
        )
    )
    val uploadList = MediatorLiveData<List<Comic>>().apply {
        addSource(uploadsLiveData) { res ->
            res.data?.let { list ->
                AsyncProcessor {
                    list.mapAsync(bag) { Source.api.getComicData(it) }
                        .sortedByDescending { it.uploadTimeMs }
                        .also { postValue(it) }
                } handleError {
                    progress.alertAsync(it.message)
                } runWith (bag)
            }
        }
    }

    fun onChannelClicked(user: User) {
    }

    fun onChannelFollowed(user: User) {

    }

    fun onFavoriteClicked(comic: Comic) {

    }

    fun onUploadClicked(comic: Comic) {

    }

    fun onUploadDeleted(comic: Comic) = clickThrottler.next {
        Source.api.removeComic(comic)
    }

    fun onPhotoChanged() {
        optionWindowClicked.value = true
        photoChangeRequested.value = true
        wallpaperChangeRequested.value = false
        showAlertChoosePhoto()
    }

    fun onWallpaperChanged() {
        optionWindowClicked.value = true
        photoChangeRequested.value = false
        wallpaperChangeRequested.value = true
        showAlertChoosePhoto()
    }

    fun onUsernameChanged() {
        optionWindowClicked.value = true
        startUsernameChange()
    }

    fun onLoggedOut() {
        optionWindowClicked.value = true
        progress.alert(
            "Are you sure you want to log out?",
            ::logOut
        )
    }

    fun onUsernameChangeFinished(newUsername: String) {
        progress.apply {
            Source.apply {
                if (newUsername != userPrefs.username)
                    AsyncProcessor {
                        loadAsync()
                        validateUsernameAsync(bag, newUsername)

                        api.changeUsername(userPrefs.uuid, newUsername)
                        finishAsync()

                        userPrefs.username = newUsername
                        username.postValue(newUsername)

                        finishUsernameChange()
                    } handleError {
                        alertAsync(it.message)
                        restoreFocus.postValue(true)
                    } runWith (bag)
                else
                    finishUsernameChange()
            }
        }

    }

    private fun startUsernameChange() {
        scrollFlags.postValue(scrollFlagsOff)
        isTitleEditable.postValue(true)
    }

    private fun finishUsernameChange() {
        scrollFlags.postValue(scrollFlagsOn)
        isTitleEditable.postValue(false)
    }

    fun onUsernameChangeCanceled(): Unit = finishUsernameChange()

    private fun logOut(accepted: Boolean) {
        if (accepted) {
            Source.userPrefs.clear()
            loggedOut.value = true
        }
        progress.finish()
    }

    private fun showAlertChoosePhoto(): Unit = showImageSelectionAlert(
        progress,
        wallpaperChangeRequested.value == true,
        ::requestPhoto
    )

    private fun requestPhoto(useCamera: Boolean) {
        useCameraForPhoto.postValue(useCamera)
        progress.finish()
    }

    private fun uploadPhoto(uri: Uri): Unit = requestAsync {
        extractBitmapFrom(uri)?.let { bitmap ->
            Source.apply {
                api.uploadUserPhoto(
                    userPrefs.uuid,
                    bitmap
                ).also {
                    userPrefs.photoDownloadUri = it
                    photoUri.postValue(it)
                }
            }
        }
    }

    private fun uploadWallpaper(uri: Uri): Unit = requestAsync {
        extractBitmapFrom(uri)?.let { bitmap ->
            Source.apply {
                api.uploadUserWallpaper(
                    userPrefs.uuid,
                    bitmap
                ).also {
                    userPrefs.wallpaperDownloadUri = it
                    wallpaperUri.postValue(it)
                }
            }
        }
    }

    fun dispatchUri(uri: Uri, isProfilePhoto: Boolean): Unit =
        if (isProfilePhoto)
            uploadPhoto(uri)
        else
            uploadWallpaper(uri)


    private fun requestAsync(action: suspend () -> Unit) {
        progress.apply {
            AsyncProcessor(Dispatchers.IO) {
                loadAsync()
                action()
                finishAsync()
            } handleError {
                alertAsync(it.message)
            } runWith (bag)
        }
    }

    private fun backgroundAsync(action: suspend () -> Unit) {
        progress.apply {
            AsyncProcessor(Dispatchers.IO) {
                action()
            } handleError {
                alertAsync(it.message)
            } runWith (bag)
        }
    }
}