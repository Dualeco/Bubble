package com.kpiroom.bubble.ui.accountSetup

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.R
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.getResUri
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.databinding.ProgressState
import com.kpiroom.bubble.util.livedata.*
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.*

class AccountSetupLogic : CoreLogic() {

    companion object {
        const val TAG = "AccountSetupLogic"
    }

    val progress = MutableLiveData<ProgressState>()

    val username = MutableLiveData<String>()
    private val usernameRegex = str(R.string.username_regex).toRegex()
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

    val backButtonClicked = MutableLiveData<Boolean>()
    fun onBackButton() {
        Source.userPrefs.clear()
        backButtonClicked.value = true
    }

    fun onPhotoChanged() {
        clearUsernameFocus.value = true
        photoChangeRequested.value = true
        wallpaperChangeRequested.value = false
    }

    fun onWallpaperChanged() {
        clearUsernameFocus.value = true
        photoChangeRequested.value = false
        wallpaperChangeRequested.value = true
    }

    fun onGetStarted() {
        clearUsernameFocus.value = true
        progress.apply {
            username.value.let {

                AsyncProcessor {
                    progress.loadAsync()
                    when {
                        it.isNullOrBlank() ->
                            alertAsync(str(R.string.setup_username_cannot_be_empty))

                        it.length < 6 ->
                            alertAsync(str(R.string.setup_username_too_short))

                        it.length > 16 ->
                            alertAsync(str(R.string.setup_username_too_long))

                        !it.matches(usernameRegex) ->
                            alertAsync(str(R.string.setup_username_invalid))

                        Source.api.usernameExists(it) ->
                            alertAsync(str(R.string.setup_username_exists))

                        !isPhotoSet || !isWallpaperSet ->
                            progress.alertAsync(
                                warningPhotosUnchanged,
                                ::isSetupFinishRequested
                            )

                        else -> {
                            finishRequested.postValue(true)
                            progress.finishAsync()
                        }
                    }
                }
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
            uuid.let { id ->
                api.uploadUserData(
                    id,
                    User(
                        username,
                        joinedDate,
                        isPhotoSet,
                        isWallpaperSet
                    )
                )
            }
        }
        uploadUserImages()
    }

    private suspend fun uploadUserImages() = Source.apply {
        val photoUri = photoUri.value ?: return@apply
        val wallpaperUri = wallpaperUri.value ?: return@apply

        userPrefs.uuid.let { id ->
            api.apply {
                if (isPhotoSet)
                    uploadUserPhoto(id, photoUri)

                if (isWallpaperSet)
                    uploadUserWallpaper(id, wallpaperUri)
            }
        }
    }

    private fun saveUserData() {
        val username = username.value ?: ""
        Source.userPrefs.let {
            it.username = username
            it.joinedDate = currentDateString
            it.isPhotoSet = isPhotoSet
            it.isWallpaperSet = isWallpaperSet
        }
    }

    fun dispatchUri(uri: Uri, isProfilePhoto: Boolean) {
        if (isProfilePhoto)
            photoUri.value = uri
        else
            wallpaperUri.value = uri
    }
}