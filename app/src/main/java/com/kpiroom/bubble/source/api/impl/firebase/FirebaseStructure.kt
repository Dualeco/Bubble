package com.kpiroom.bubble.source.api.impl.firebase

import java.util.*

object FirebaseStructure {

    private val VERSION_KEY = "version"
    private val META_KEY = "meta"
    val USERS = "users"
    val USERNAMES = "usernames"
    val IS_CONNECTED = ".info/connected"

    val VERSION = "/$VERSION_KEY"

    object META { // E. g.

        private val SERVER_TIME_KEY = "server_time"

        val SERVER_TIME = "/$META_KEY/$SERVER_TIME_KEY"
    }

    data class User(
        val username: String?,
        val joinedDate: String?
    )
}