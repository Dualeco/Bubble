package com.kpiroom.bubble.ui.main.fragments.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.ui.main.fragments.profile.tabs.FavouritesTabFragment
import com.kpiroom.bubble.ui.main.fragments.profile.tabs.SubscriptionsTabFragment
import com.kpiroom.bubble.ui.main.fragments.profile.tabs.UploadsTabFragment
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.bitmap.extractBitmapFrom
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.imageUpload.showImageSelectionAlert
import com.kpiroom.bubble.util.livedata.setDefault
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.progressState.livedata.*
import com.kpiroom.bubble.util.usernameValidation.validateUsernameAsync
import kotlinx.coroutines.Dispatchers

class ProfileLogic : CoreLogic() {
    val scrollFlagsOn = SCROLL_FLAG_SCROLL or SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
    val scrollFlagsOff = 0

    val restoreFocus = MutableLiveData<Boolean>()

    val scrollFlags = MutableLiveData<Int>().setDefault(scrollFlagsOn)

    val progress = MutableLiveData<ProgressState>()

    val username = MutableLiveData<String>().setDefault(Source.userPrefs.username)

    val isTitleEditable = MutableLiveData<Boolean>()

    val joinedOn = Source.userPrefs.joinedDate

    val useCameraForPhoto = MutableLiveData<Boolean>()

    lateinit var photoSourceUri: Uri
    lateinit var wallpaperSourceUri: Uri

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

    val wallpaperChangeRequested = MutableLiveData<Boolean>()

    val optionWindowClicked = MutableLiveData<Boolean>()
    val loggedOut = MutableLiveData<Boolean>()

    val fragmentChannels = SubscriptionsTabFragment.newInstance(str(R.string.profile_channels))
    val fragmentFavorites = FavouritesTabFragment.newInstance(str(R.string.profile_favorites))
    val fragmentUploads = UploadsTabFragment.newInstance(str(R.string.profile_uploads))

    val pagerFragments = arrayListOf(
        fragmentChannels,
        fragmentFavorites,
        fragmentUploads
    )

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
}