package com.kpiroom.bubble.ui.accountSetup

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.R
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.bitmap.extractBitmapFrom
import com.kpiroom.bubble.util.constants.getResUri
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.imageUpload.showImageSelectionAlert
import com.kpiroom.bubble.util.livedata.progressState.alert
import com.kpiroom.bubble.util.livedata.progressState.alertAsync
import com.kpiroom.bubble.util.livedata.progressState.finishAsync
import com.kpiroom.bubble.util.livedata.progressState.loadAsync
import com.kpiroom.bubble.util.livedata.setDefault
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.usernameValidation.validateUsernameAsync
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.*

class AccountSetupLogic : CoreLogic() {

    companion object {
        const val TAG = "AccountSetupLogic"
    }

    val progress = MutableLiveData<ProgressState>()

    val username = MutableLiveData<String>()
    val clearUsernameFocus = MutableLiveData<Boolean>()

    private val dateFormat = str(R.string.date_format)
    private val currentDateString = SimpleDateFormat(dateFormat).format(Date()).toString()
    val joinedOn = "${str(R.string.setup_joined_on)} $currentDateString"

    val photoChangeRequested = MutableLiveData<Boolean>()
    val wallpaperChangeRequested = MutableLiveData<Boolean>()

    val finishRequested = MutableLiveData<Boolean>()
    val setupComplete = MutableLiveData<Boolean>()

    private val defaultPhotoUri = getResUri(R.drawable.default_avatar)
    val photoUri = MutableLiveData<Uri>().setDefault(defaultPhotoUri)

    private val defaultWallpaperUri = getResUri(R.drawable.default_wallpaper)
    val wallpaperUri = MutableLiveData<Uri>().setDefault(defaultWallpaperUri)

    private val isPhotoSet
        get() = photoUri.value != defaultPhotoUri
    private val isWallpaperSet
        get() = wallpaperUri.value != defaultWallpaperUri

    val useCameraForPhoto = MutableLiveData<Boolean>()

    val backButtonClicked = MutableLiveData<Boolean>()
    fun onBackButton() {
        Source.userPrefs.clear()
        backButtonClicked.value = true
    }

    fun onPhotoChanged() {
        clearUsernameFocus.value = true
        photoChangeRequested.value = true
        wallpaperChangeRequested.value = false
        showAlertChoosePhoto()
    }

    fun onWallpaperChanged() {
        clearUsernameFocus.value = true
        photoChangeRequested.value = false
        wallpaperChangeRequested.value = true
        showAlertChoosePhoto()
    }

    fun onGetStarted() {
        clearUsernameFocus.value = true
        progress.apply {
            AsyncProcessor {
                loadAsync()
                validateUsernameAsync(bag, username.value)
                finishAsync()

                if (!isPhotoSet || !isWallpaperSet)
                    alertAsync(
                        warningPhotosUnchanged,
                        ::isSetupFinishRequested
                    )
                else
                    finishRequested.postValue(true)

            } handleError {
                progress.alert(it.message)
            } runWith (bag)
        }
    }

    private val warningPhotosUnchanged
        get() = app.getString(
            R.string.setup_continue_with_unchanged,
            when {
                !isPhotoSet && !isWallpaperSet -> str(R.string.setup_unchanged_photo_and_wallpaper)
                !isPhotoSet -> str(R.string.setup_unchanged_photo)
                !isWallpaperSet -> str(R.string.setup_unchanged_wallpaper)
                else -> null
            }
        )

    private fun isSetupFinishRequested(isRequested: Boolean) {
        if (isRequested)
            finishRequested.postValue(true)
    }

    fun finishSetup() {
        progress.apply {
            AsyncProcessor(Dispatchers.IO) {
                loadAsync()

                saveUserData()
                uploadProfile()
                setupComplete.postValue(true)

                finishAsync()
            } handleError {
                alert(it.message)
            } runWith (bag)
        }
    }

    private suspend fun uploadProfile() = Source.apply {
        userPrefs.apply {
            api.uploadUserData(
                uuid,
                User(
                    uuid,
                    username,
                    joinedDate
                )
            )
        }
        uploadUserImages()
    }

    private suspend fun uploadUserImages() = Source.apply {
        val photoUri = photoUri.value ?: return@apply
        val wallpaperUri = wallpaperUri.value ?: return@apply

        userPrefs.apply {
            if (isPhotoSet)
                extractBitmapFrom(photoUri)?.let { bitmap ->
                    photoDownloadUri = api.uploadUserPhoto(uuid, bitmap)
                }
            if (isWallpaperSet)
                extractBitmapFrom(wallpaperUri)?.let { bitmap ->
                    wallpaperDownloadUri = api.uploadUserWallpaper(uuid, bitmap)
                }
        }
    }

    private fun saveUserData() {
        Source.userPrefs.let {
            it.username = username.value ?: ""
            it.joinedDate = currentDateString
        }
    }

    fun dispatchUri(uri: Uri, isProfilePhoto: Boolean) {
        if (isProfilePhoto)
            photoUri.value = uri
        else
            wallpaperUri.value = uri
    }

    fun showAlertChoosePhoto(): Unit = showImageSelectionAlert(
        progress,
        wallpaperChangeRequested.value == true,
        ::requestPhoto
    )

    private fun requestPhoto(useCamera: Boolean) {
        useCameraForPhoto.postValue(useCamera)
    }
}