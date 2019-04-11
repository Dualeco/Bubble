package com.kpiroom.bubble.util.recyclerview.model

import com.kpiroom.bubble.source.api.impl.firebase.FirebaseStructure.Comparable

data class NamedComic(
    val uuid: String = "",
    val title: String = "",
    val thumbnailUrl: String = "",
    val description: String = "",
    val authorName: String = "",
    val uploadTimeMs: Long = 0L,
    val downloads: Int = 0,
    val stars: Int = 0
) : Comparable(uuid, title)