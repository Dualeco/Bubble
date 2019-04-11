package com.kpiroom.bubble.source.api.impl.firebase

import java.util.*

object FirebaseStructure {

    private const val VERSION_KEY = "version"
    private const val META_KEY = "meta"
    const val USERS = "users"
    const val USER_UUIDS = "user_uuids"
    const val COMICS = "comics"
    const val PREVIEWS = "previews"
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

    abstract class Comparable(
        val id: String,
        val name: String
    )

    data class User(
        val uuid: String = "",
        val username: String = "",
        val joinedDate: String = "",
        val photoUrl: String = "",
        val wallpaperUrl: String = ""
    ) : Comparable(uuid, username)

    data class Comic(
        val uuid: String = "",
        val title: String = "",
        val thumbnailUrl: String = "",
        val previewUrl: String = "",
        val description: String = "",
        val authorId: String = "",
        val uploadTimeMs: Long = 0L,
        val downloads: Int = 0,
        val stars: Int = 0
    ) : Comparable(uuid, title)

    class USER_KEYS(uuid: String) {
        val LOCATION = "$USERS/$uuid"
        val UUID = "$LOCATION/uuid"
        val USERNAME = "$LOCATION/username"
        val JOINED_DATE = "$LOCATION/joinedDate"
        val PHOTO_URL = "$LOCATION/photoUrl"
        val WALLPAPER_URL = "$LOCATION/wallpaperUrl"
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