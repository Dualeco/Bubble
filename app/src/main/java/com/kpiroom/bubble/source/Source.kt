package com.kpiroom.bubble.source

import com.kpiroom.bubble.source.api.ApiInterface
import com.kpiroom.bubble.source.api.impl.firebase.FirebaseApi
import com.kpiroom.bubble.source.db.DbInterface
import com.kpiroom.bubble.source.db.impl.room.RoomDb
import com.kpiroom.bubble.source.pref.UserPrefs

object Source {

    val api: ApiInterface = FirebaseApi()

    val db: DbInterface = RoomDb()

    val userPrefs = UserPrefs()
}