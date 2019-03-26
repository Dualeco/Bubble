package com.kpiroom.bubble.ui.main.fragments.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.dichotome.profilebar.stubs.fragments.FavouritesTabFragment
import com.dichotome.profilebar.stubs.fragments.SubscriptionsTabFragment
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.DIR_PROFILE_PHOTOS
import com.kpiroom.bubble.util.constants.DIR_PROFILE_WALLPAPERS
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.files.getProfilePhotoUri
import com.kpiroom.bubble.util.files.getProfileWallpaperUri
import com.kpiroom.bubble.util.files.profilePhotoExists
import com.kpiroom.bubble.util.files.profileWallpaperExists
import com.kpiroom.bubble.util.imageSelection.getUpdatedProfilePhoto
import com.kpiroom.bubble.util.imageSelection.getUpdatedProfileWallpaper
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

    var photoUri = MutableLiveData<Uri>().apply {
        Source.userPrefs.apply {
            if (isPhotoSet)
                value = getProfilePhotoUri(photoName)
        }
    }

    val photoChangeRequested = MutableLiveData<Boolean>()

    var wallpaperUri = MutableLiveData<Uri>().apply {
        Source.userPrefs.apply {
            if (isWallpaperSet)
                value = getProfileWallpaperUri(wallpaperName)
        }
    }

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

    private fun uploadPhoto(uri: Uri): Unit = uploadAsync {
        Source.apply {
            api.uploadUserPhoto(
                userPrefs.uuid,
                getUpdatedProfilePhoto(uri)
            )
        }
    }

    private fun uploadWallpaper(uri: Uri): Unit = uploadAsync {
        Source.apply {
            api.uploadUserWallpaper(
                userPrefs.uuid,
                getUpdatedProfileWallpaper(uri)
            )
        }
    }

    private fun updatePhotoFile() = downloadAsync {
        Source.apply {
            userPrefs.apply {
                if (!profilePhotoExists(photoName)) {
                    api.downloadUserPhoto(
                        photoName,
                        File(DIR_PROFILE_PHOTOS, photoName)
                    )
                    photoUri.postValue(getProfilePhotoUri(photoName))
                }
            }
        }
    }

    private fun updateWallpaperFile() = downloadAsync {
        Source.apply {
            userPrefs.apply {
                if (!profileWallpaperExists(wallpaperName)) {
                    api.downloadUserWallpaper(
                        wallpaperName,
                        File(DIR_PROFILE_WALLPAPERS, wallpaperName)
                    )
                    wallpaperUri.postValue(getProfileWallpaperUri(wallpaperName))
                }
            }
        }
    }

    fun updateProfileImages() {
        Source.userPrefs.apply {
            if (isPhotoSet) {
                //refreshPhotoUri()
                updatePhotoFile()
            }

            if (isWallpaperSet) {
                //refreshWallpaperUri()
                updateWallpaperFile()
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

    private fun uploadAsync(action: suspend () -> Unit) {
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

    private fun downloadAsync(action: suspend () -> Unit) {
        progress.apply {
            AsyncProcessor(Dispatchers.IO) {
                action()
            } handleError {
                alertAsync(it.message)
            } runWith (bag)
        }
    }
}