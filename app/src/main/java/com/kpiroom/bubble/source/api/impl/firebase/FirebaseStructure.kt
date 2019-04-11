package com.kpiroom.bubble.source.api.impl.firebase

object FirebaseStructure {

    private const val VERSION_KEY = "version"
    private const val META_KEY = "meta"
    const val USERS = "users"
    const val USER_UUIDS = "user_uuids"
    const val IS_CONNECTED = ".info/connected"
    const val PROFILE_PHOTOS = "profile_photos"
    const val PROFILE_WALLPAPERS = "profile_wallpapers"

    const val VERSION = "/$VERSION_KEY"

    object META { // E. g.

        private val SERVER_TIME_KEY = "server_time"

        val SERVER_TIME = "/$META_KEY/$SERVER_TIME_KEY"
    }

    data class User(
        val username: String = "",
        val joinedDate: String = "",
        val photoUrl: String = "",
        val wallpaperUrl: String = ""
    )

    class USER_KEYS(uuid: String) {
        val LOCATION = "$USERS/$uuid"
        val USERNAME = "$LOCATION/username"
        val JOINED_DATE = "$LOCATION/joinedDate"
        val PHOTO_URL = "$LOCATION/photoUrl"
        val WALLPAPER_URL = "$LOCATION/wallpaperUrl"
    }
}