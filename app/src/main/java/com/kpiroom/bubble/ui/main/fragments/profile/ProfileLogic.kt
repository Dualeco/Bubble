package com.kpiroom.bubble.ui.main.fragments.profile

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.dichotome.profilebar.stubs.fragments.FavouritesTabFragment
import com.dichotome.profilebar.stubs.fragments.SubscriptionsTabFragment
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.imageSelection.showImageSelectionAlert
import com.kpiroom.bubble.util.livedata.setDefault
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.progressState.livedata.*
import kotlinx.coroutines.Dispatchers
import java.io.File

class ProfileLogic : CoreLogic() {
    val progress = MutableLiveData<ProgressState>()

    val username = MutableLiveData<String>().setDefault(Source.userPrefs.username)
    val usernameChangeRequested = MutableLiveData<Boolean>()

    val joinedOn = Source.userPrefs.joinedDate

    val useCameraForPhoto = MutableLiveData<Boolean>()

    var photoUri = MutableLiveData<Uri>()
    val photoChangeRequested = MutableLiveData<Boolean>()

    var wallpaperUri = MutableLiveData<Uri>()
    val wallpaperChangeRequested = MutableLiveData<Boolean>()

    val optionWindowClicked = MutableLiveData<Boolean>()
    val loggedOut = MutableLiveData<Boolean>()

    val titleSubscriptions = str(R.string.profile_subscriptions)
    val titleFavorites = str(R.string.profile_favorites)

    val pagerFragments = arrayListOf(
        SubscriptionsTabFragment.newInstance(titleSubscriptions),
        FavouritesTabFragment.newInstance(titleFavorites)
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
        progress.alert(
            "Are you sure you want to change username?",
            ::changeUsername
        )
    }

    fun onLoggedOut() {
        optionWindowClicked.value = true
        progress.alert(
            "Are you sure you want to log out?",
            ::logOut
        )
    }

    private fun changeUsername(accepted: Boolean) {
        if (accepted)
            progress.input {
                username.value = it
            }
        progress.finish()
    }

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

    fun downloadWallpaperFile(destination: File): Unit =
        loadAsync {
            Source.apply {
                api.downloadUserWallpaper(
                    userPrefs.uuid,
                    destination
                )
            }
            wallpaperUri.postValue(destination.toUri())
        }

    fun downloadPhotoFile(destination: File): Unit = loadAsync {
        Source.apply {
            api.downloadUserPhoto(
                userPrefs.uuid,
                destination
            )
        }
        photoUri.postValue(destination.toUri())
    }


    private fun uploadPhoto(uri: Uri): Unit = loadAsync {
        Source.apply {
            userPrefs.uuid?.let { id ->
                api.uploadUserPhoto(id, uri)
            }
        }
    }

    private fun uploadWallpaper(uri: Uri): Unit = loadAsync {
        Source.apply {
            userPrefs.uuid?.let { id ->
                api.uploadUserWallpaper(id, uri)
            }
        }
    }

    fun dispatchUri(uri: Uri, isProfilePhoto: Boolean): Unit =
        if (isProfilePhoto) {
            uploadPhoto(uri)
            photoUri.value = uri
        } else {
            uploadWallpaper(uri)
            wallpaperUri.value = uri
        }

    private fun loadAsync(action: suspend () -> Unit) {
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