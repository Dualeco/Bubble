package com.kpiroom.bubble.util.recyclerview.model

import android.os.Parcel
import android.os.Parcelable
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comparable
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.User
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comic

data class ComicPage(
    override val id: String = "",
    override val name: String = "",
    val thumbnailUrl: String = "",
    val previewUrl: String = "",
    val description: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorThumbnailUrl: String = "",
    val uploadTimeMs: Long = 0L,
    val downloads: Int = 0,
    val stars: Int = 0
) : Comparable, Parcelable {

    constructor(comic: Comic, user: User) : this(
        comic.id,
        comic.name,
        comic.thumbnailUrl,
        comic.previewUrl,
        comic.description,
        user.id,
        user.name,
        user.photoUrl,
        comic.uploadTimeMs
    )

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(thumbnailUrl)
        parcel.writeString(previewUrl)
        parcel.writeString(description)
        parcel.writeString(authorId)
        parcel.writeString(authorName)
        parcel.writeString(authorThumbnailUrl)
        parcel.writeLong(uploadTimeMs)
        parcel.writeInt(downloads)
        parcel.writeInt(stars)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ComicPage> {
        override fun createFromParcel(parcel: Parcel): ComicPage {
            return ComicPage(parcel)
        }

        override fun newArray(size: Int): Array<ComicPage?> {
            return arrayOfNulls(size)
        }
    }
}