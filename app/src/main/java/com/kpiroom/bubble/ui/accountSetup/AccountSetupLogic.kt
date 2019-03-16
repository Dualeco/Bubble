package com.kpiroom.bubble.ui.accountSetup

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kpiroom.bubble.R
import com.kpiroom.bubble.os.BubbleApp.Companion.app
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.getResUri
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.databinding.ProgressState
import com.kpiroom.bubble.util.livedata.alert
import com.kpiroom.bubble.util.livedata.finishAsync
import com.kpiroom.bubble.util.livedata.load
import java.text.SimpleDateFormat
import java.util.*

class AccountSetupLogic : CoreLogic() {
    val progress = MutableLiveData<ProgressState>()

    val username = MutableLiveData<String>()
    private val usernameRegex = str(R.string.username_regex).toRegex()
    val clearUsernameFocus = MutableLiveData<Boolean>()

    private val dateFormat = str(R.string.date_format)
    val dateString = "${str(R.string.setup_joined_on)} ${SimpleDateFormat(dateFormat).format(Date())}"

    val photoChangeRequested = MutableLiveData<Boolean>()
    val wallpaperChangeRequested = MutableLiveData<Boolean>()

    val started = MutableLiveData<Boolean>()

    val photoDrw = MutableLiveData<Drawable>()
    val wallpaperPhotoDrw = MutableLiveData<Drawable>()

    private var photoUri = getResUri(R.drawable.default_avatar)
    private var wallpaperUri = getResUri(R.drawable.default_wallpaper)

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
        username.value.let {
            when {
                it.isNullOrBlank() -> {
                    progress.alert(str(R.string.setup_username_cannot_be_empty))
                    return
                }
                usernameExists(it) -> {
                    progress.alert(str(R.string.setup_username_exists))
                    return
                }
                else -> {
                    if (it.length < 6) {
                        progress.alert(str(R.string.setup_username_too_short))
                        return
                    } else if (it.length > 16) {
                        progress.alert(str(R.string.setup_username_too_long))
                        return
                    } else if (!it.matches(usernameRegex)) {
                        progress.alert(str(R.string.setup_username_invalid))
                        return
                    } else {
                        if (photoDrw.value == null || wallpaperPhotoDrw.value == null) {
                            progress.alert(
                                getUnchangedDrwMessage(),
                                ::finishSetupCondition
                            )
                        } else finishSetup()
                    }
                }
            }
        }
    }

    private fun getUnchangedDrwMessage(): String? {
        val photo = photoDrw.value
        val wallpaper = wallpaperPhotoDrw.value

        val unchangedDrwString = if (photo == null && wallpaper == null)
            "photo and wallpaper"
        else if (photo == null)
            "photo"
        else if (wallpaper == null)
            "wallpaper"
        else null

        return app.getString(R.string.continue_with_unchanged, unchangedDrwString)
    }

    private fun finishSetupCondition(condition: Boolean) {
        if (condition)
            finishSetup()
        else
            return
    }

    private fun finishSetup() {
        saveProfileData()
        started.value = true
    }

    private fun saveProfileData() {
        Source.userPrefs.let {
            it.username = username.value
            it.joinedDateString = dateString
            it.profilePhotoUri = photoUri
            it.profileWallpaperUri = wallpaperUri
        }
    }

    private fun usernameExists(username: String): Boolean {
        var exists = false
        AsyncProcessor {
            exists = Source.api.usernameExists(username)
        } handleError {
            progress.alert(it.message)
        } runWith (bag)

        return exists
    }

    fun loadPhoto(uri: Uri, asProfilePhoto: Boolean) {
        val imageBitmap: MutableLiveData<Drawable>
        val options = RequestOptions()

        if (asProfilePhoto) {
            options.circleCrop()

            photoUri = uri
            imageBitmap = photoDrw
        } else {
            wallpaperUri = uri
            imageBitmap = wallpaperPhotoDrw
        }

        progress.load()
        AsyncProcessor {
            imageBitmap.postValue(
                Glide.with(app)
                    .asDrawable()
                    .load(uri)
                    .apply(options)
                    .submit()
                    .get()
            )
            progress.finishAsync()
        } handleError {
            progress.alert(it.message)
        } runWith (bag)
    }
}