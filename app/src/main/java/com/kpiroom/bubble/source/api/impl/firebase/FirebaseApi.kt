package com.kpiroom.bubble.source.api.impl.firebase

import androidx.lifecycle.LiveData
import com.google.firebase.database.FirebaseDatabase
import com.kpiroom.bubble.source.api.ApiInterface
import kotlinx.coroutines.Deferred

class FirebaseApi : ApiInterface {

    private val util = FirebaseUtil(FirebaseDatabase.getInstance().apply {
        setPersistenceEnabled(false)
    })

    override suspend fun getServerVersion(): String = util.read(FirebaseStructure.VERSION, String::class.java)

    override suspend fun setServerVersion(version: String) = util.write(FirebaseStructure.VERSION, version)
}