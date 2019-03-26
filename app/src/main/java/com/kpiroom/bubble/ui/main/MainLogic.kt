package com.kpiroom.bubble.ui.main

import com.kpiroom.bubble.source.Source
import com.kpiroom.bubble.ui.core.CoreLogic
import com.kpiroom.bubble.util.async.AsyncProcessor
import com.kpiroom.bubble.util.constants.DIR_PROFILE_PHOTOS
import com.kpiroom.bubble.util.constants.DIR_PROFILE_WALLPAPERS
import com.kpiroom.bubble.util.files.getProfilePhotoUri
import com.kpiroom.bubble.util.files.getProfileWallpaperUri
import java.io.File

class MainLogic : CoreLogic() {

    fun fetchUserData() {
        AsyncProcessor {
            Source.apply {
                userPrefs.apply {
                    api.updateUserPrefs(userPrefs.uuid)
                }
            }
        } runWith (bag)
    }
}