package com.kpiroom.bubble.ui.main.fragments.profile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.dichotome.profilebar.stubs.FavListItem
import com.dichotome.profileshared.extensions.addTo
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
import com.kpiroom.bubble.R
import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.ui.main.fragments.profile.tabs.FavouritesTabFragment
import com.kpiroom.bubble.ui.main.fragments.profile.tabs.SubscriptionsTabFragment
import com.kpiroom.bubble.ui.main.fragments.profile.tabs.UploadsTabFragment
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.DIR_PROFILE_PHOTOS
import com.kpiroom.bubble.util.constants.DIR_PROFILE_WALLPAPERS
import com.kpiroom.bubble.util.constants.str
import com.kpiroom.bubble.util.files.getProfilePhotoUri
import com.kpiroom.bubble.util.files.getProfileWallpaperUri
import com.kpiroom.bubble.util.files.profilePhotoExists
import com.kpiroom.bubble.util.files.profileWallpaperExists
import com.kpiroom.bubble.util.imageUpload.getUpdatedProfilePhoto
import com.kpiroom.bubble.util.imageUpload.getUpdatedProfileWallpaper
import com.kpiroom.bubble.util.imageUpload.showImageSelectionAlert
import com.kpiroom.bubble.util.livedata.setDefault
import com.kpiroom.bubble.util.progressState.ProgressState
import com.kpiroom.bubble.util.progressState.livedata.*
import com.kpiroom.bubble.util.usernameValidation.validateUsername
import kotlinx.coroutines.Dispatchers
import java.io.File

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
        fragmentFavorites.items = listOf(
            FavListItem(
                "Star Wars",
                "#55, 2008",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
            ),
            FavListItem(
                "Esteemed comic book author",
                "#214, 2017",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
            ),
            FavListItem(
                "Superior comic book writer",
                "#3, 2019",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
            ),
            FavListItem(
                "Batman: Rebirth",
                "#4, 2011",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccovers2017/large/batman26-mikeljanin.png?1384968217"
            ),
            FavListItem(
                "The Amazing Spider-Man",
                "#5, 2018",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
            ),
            FavListItem(
                "Star Wars",
                "#55, 2008",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
            ),
            FavListItem(
                "Esteemed comic book author",
                "#214, 2017",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/amazing-spider-man--2-cover-art-by-ryan-ottley.png?1384968217"
            ),
            FavListItem(
                "Superior comic book writer",
                "#3, 2019",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccoversof2018/large/star-wars--55-cover-art-by-david-marquez.png?1384968217"
            ),
            FavListItem(
                "Batman: Rebirth",
                "#4, 2011",
                "https://cdn.pastemagazine.com/www/system/images/photo_albums/bestcomiccovers2017/large/batman26-mikeljanin.png?1384968217"
            )
        )
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
        if (accepted) startUsernameChange()
        progress.finish()
    }

    fun onUsernameChangeFinished(newUsername: String) {
        progress.apply {
            Source.apply {
                if (newUsername != userPrefs.username)
                    AsyncProcessor {
                        loadAsync()
                        validateUsername(newUsername)

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
        Source.apply {
            api.uploadUserPhoto(
                userPrefs.uuid,
                getUpdatedProfilePhoto(uri)
            )
        }
    }

    private fun uploadWallpaper(uri: Uri): Unit = requestAsync {
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
            if (isPhotoSet)
                updatePhotoFile()

            if (isWallpaperSet)
                updateWallpaperFile()
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