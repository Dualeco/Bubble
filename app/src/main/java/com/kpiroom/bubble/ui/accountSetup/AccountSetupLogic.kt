package com.kpiroom.bubble.ui.accountSetup

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.kpiroom.bubble.R
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.getResUri
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.databinding.ProgressState
import com.kpiroom.bubble.util.livedata.*
import java.text.SimpleDateFormat
import java.util.*

class AccountSetupLogic : CoreLogic() {
    val progress = MutableLiveData<ProgressState>()

    val username = MutableLiveData<String>()
    private val usernameRegex = str(R.string.username_regex).toRegex()
    val clearUsernameFocus = MutableLiveData<Boolean>()

    private val dateFormat = str(R.string.date_format)
    private val dateString = SimpleDateFormat(dateFormat).format(Date()).toString()
    val joinedOn = "${str(R.string.setup_joined_on)} $dateString"

    val photoChangeRequested = MutableLiveData<Boolean>()
    val wallpaperChangeRequested = MutableLiveData<Boolean>()

    val finishRequested = MutableLiveData<Boolean>()
    val started = MutableLiveData<Boolean>()

    private val defaultPhotoUri = getResUri(R.drawable.default_avatar)
    val photoUri = MutableLiveData<Uri>().setDefault(
        defaultPhotoUri
    )
    private val isPhotoDefault
        get() = photoUri.value == defaultPhotoUri

    private val defaultWallpaperUri = getResUri(R.drawable.default_wallpaper)
    val wallpaperUri = MutableLiveData<Uri>().setDefault(
        defaultWallpaperUri
    )
    private val isWallpaperDefault
        get() = wallpaperUri.value == defaultWallpaperUri

    val backButtonClicked = MutableLiveData<Boolean>()
    fun onBackButton() {
        Source.userPrefs.uuid = null
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

                Log.d("START", "GET")

                AsyncProcessor {
                    progress.loadAsync()
                    when {
                        it.isNullOrBlank() -> alertAsync(str(R.string.setup_username_cannot_be_empty))

                        it.length < 6 -> alertAsync(str(R.string.setup_username_too_short))

                        it.length > 16 -> alertAsync(str(R.string.setup_username_too_long))

                        !it.matches(usernameRegex) -> alertAsync(str(R.string.setup_username_invalid))

                        Source.api.usernameExists(it) -> alertAsync(str(R.string.setup_username_exists))

                        isPhotoDefault || isWallpaperDefault -> {
                            progress.alertAsync(
                                unchangedDrwMessage,
                                ::isSetupFinishRequested
                            )
                        }

                        else -> {
                            finishAsync()
                            finishRequested.postValue(true)
                        }
                    }
                }
            } handleError {
                progress.alert(it.message)
            } runWith (bag)
        }
    }

    private val unchangedDrwMessage
        get() = app.getString(
            R.string.setup_continue_with_unchanged,
            when {
                isPhotoDefault && isWallpaperDefault -> str(R.string.setup_unchanged_photo_and_wallpaper)
                isPhotoDefault -> str(R.string.setup_unchanged_photo)
                isWallpaperDefault -> str(R.string.setup_unchanged_wallpaper)
                else -> null
            }
        )

    private fun isSetupFinishRequested(isRequested: Boolean) {
        if (isRequested)
            finishRequested.postValue(true)
        else
            return
    }

    fun finishSetup() {
        progress.apply {
            saveProfileData()

            AsyncProcessor {
                loadAsync()
                Source.api.setUpAccount()

                finishAsync()
                started.postValue(true)
            } handleError {
                alert(it.message)
            } runWith (bag)
        }
    }

    private fun saveProfileData() {
        Source.userPrefs.let {
            it.username = username.value
            it.joinedDateString = dateString
            it.profilePhotoUri = photoUri.value
            it.profileWallpaperUri = wallpaperUri.value
        }
    }

    fun dispatchUri(uri: Uri, isProfilePhoto: Boolean) {
        if (isProfilePhoto)
            photoUri.value = uri
        else
            wallpaperUri.value = uri
    }
}