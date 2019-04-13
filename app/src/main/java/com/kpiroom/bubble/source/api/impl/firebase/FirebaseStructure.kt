package com.kpiroom.bubble.source.api.impl.firebase

import android.os.Parcel
import android.os.Parcelable

object FirebaseStructure {

    private const val VERSION_KEY = "version"
    private const val META_KEY = "meta"
    const val USERS = "users"
    const val USER_UUIDS = "user_uuids"
    const val COMICS = "comics"
    const val PREVIEWS = "previews"
    const val FAV_PREVIEWS = "fav_previews"
    const val THUMBNAILS = "thumbnails"
    const val IS_CONNECTED = ".info/connected"
    const val PROFILE_PHOTOS = "profile_photos"
    const val PROFILE_THUMBNAILS = "profile_thumbnails"
    const val PROFILE_WALLPAPERS = "profile_wallpapers"

    const val VERSION = "/$VERSION_KEY"

    object META { // E. g.

        private val SERVER_TIME_KEY = "server_time"

        val SERVER_TIME = "/$META_KEY/$SERVER_TIME_KEY"
    }

    interface Comparable {
        val id: String
        val name: String
    }

    data class TimeRecord<T>(
        val data: T,
        val time: Long
    )

    data class User(
        override val id: String = "",
        override val name: String = "",
        val joinedDate: String = "",
        val photoUrl: String = "",
        val thumbnailUrl: String = "",
        val wallpaperUrl: String = ""
    ) : Comparable, Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(id)
            parcel.writeString(name)
            parcel.writeString(joinedDate)
            parcel.writeString(photoUrl)
            parcel.writeString(wallpaperUrl)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<User> {
            override fun createFromParcel(parcel: Parcel): User {
                return User(parcel)
            }

            override fun newArray(size: Int): Array<User?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class Comic(
        override val id: String = "",
        override val name: String = "",
        val thumbnailUrl: String = "",
        val favPreviewUrl: String = "",
        val previewUrl: String = "",
        val description: String = "",
        val authorId: String = "",
        val uploadTimeMs: Long = 0L
    ) : Comparable

    class USER_KEYS(uuid: String) {
        val LOCATION = "$USERS/$uuid"
        val UUID = "$LOCATION/id"
        val USERNAME = "$LOCATION/name"
        val JOINED_DATE = "$LOCATION/joinedDate"
        val PHOTO_URL = "$LOCATION/photoUrl"
        val THUMBNAIL_URL = "$LOCATION/thumbnailUrl"
        val WALLPAPER_URL = "$LOCATION/wallpaperUrl"
        val UPLOADS = "$LOCATION/uploads"
        val FAVORITES = "$LOCATION/favorites"
        val SUBSCRIPTIONS = "$LOCATION/subscriptions"
        val SUBSCRIBERS = "$LOCATION/subscribers"
    }

    class COMIC_KEYS(uuid: String) {
        val LOCATION = "$COMICS/$uuid"
        val UUID = "$LOCATION/id"
        val TITLE = "$LOCATION/name"
        val THUMBNAIL_URL = "$LOCATION/thumbnailUrl"
        val DESCRIPTION = "$LOCATION/description"
        val AUTHOR_ID = "$LOCATION/authorId"
        val UPLOAD_TIME_MS = "$LOCATION/uploadTimeMs"
        val DOWNLOADS = "$LOCATION/downloads"
        val STARRED = "$LOCATION/starred"
    }
}