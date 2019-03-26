package com.kpiroom.bubble.source.api.impl.firebase

object FirebaseStructure {

    private const val VERSION_KEY = "version"
    private const val META_KEY = "meta"
    const val USERS = "users"
    const val USERNAMES = "usernames"
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
        val photoSet: Boolean = false,
        val wallpaperSet: Boolean = false,
        val photoName: String = "",
        val wallpaperName: String = ""
    )

    class USER_KEYS(uuid: String) {
        val LOCATION = "$USERS/$uuid"
        val USERNAME = "$LOCATION/username"
        val JOINED_DATE = "$LOCATION/joinedDate"
        val PHOTO_SET = "$LOCATION/photoSet"
        val WALLPAPER_SET = "$LOCATION/wallpaperSet"
        val PHOTO_NAME = "$LOCATION/photoName"
        val WALLPAPER_NAME = "$LOCATION/wallpaperName"
    }
}