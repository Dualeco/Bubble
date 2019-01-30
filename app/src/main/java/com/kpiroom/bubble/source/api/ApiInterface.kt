package com.kpiroom.bubble.source.api

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Deferred

interface ApiInterface {

    suspend fun getServerVersion(): String

    suspend fun setServerVersion(version: String)
}