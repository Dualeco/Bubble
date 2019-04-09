package com.kpiroom.bubble.source.api.impl.firebase

import java.util.*

object FirebaseStructure {

    private const val VERSION_KEY = "version"
    private const val META_KEY = "meta"
    const val USERS = "users"
    const val COMICS = "comics"
    const val THUMBNAILS = "thumbnails"
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
        val uuid: String = "",
        val username: String = "",
        val joinedDate: String = "",
        val photoName: String = "",
        val wallpaperName: String = ""
    )

    data class Comic(
        val uuid: String = "",
        val title: String = "",
        val thumbnailUrl: String = "",
        val description: String = "",
        val authorId: String = "",
        val uploadTimeMs: Long = 0L,
        val downloads: Int = 0,
        val stars: Int = 0
    )

    class USER_KEYS(uuid: String) {
        val LOCATION = "$USERS/$uuid"
        val UUID = "$LOCATION/uuid"
        val USERNAME = "$LOCATION/username"
        val JOINED_DATE = "$LOCATION/joinedDate"
        val PHOTO_NAME = "$LOCATION/photoName"
        val WALLPAPER_NAME = "$LOCATION/wallpaperName"
        val UPLOADS = "$LOCATION/uploads"
        val FAVORITES = "$LOCATION/favorites"
        val SUBSCRIPTIONS = "$LOCATION/subscriptions"
    }

    class COMIC_KEYS(uuid: String) {
        val LOCATION = "$COMICS/$uuid"
        val UUID = "$LOCATION/uuid"
        val TITLE = "$LOCATION/title"
        val THUMBNAIL_URL = "$LOCATION/thumbnailUrl"
        val DESCRIPTION = "$LOCATION/description"
        val AUTHOR_ID = "$LOCATION/authorId"
        val UPLOAD_TIME_MS = "$LOCATION/uploadTimeMs"
        val DOWNLOADS = "$LOCATION/downloads"
        val STARS = "$LOCATION/stars"
    }
}