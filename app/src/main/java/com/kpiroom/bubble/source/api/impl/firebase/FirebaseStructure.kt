package com.kpiroom.bubble.source.api.impl.firebase

object FirebaseStructure {

    private val VERSION_KEY = "version"
    private val META_KEY = "meta"
    private val USER_KEY = "user"

    val VERSION = "/$VERSION_KEY"

    object META { // E. g.

        private val SERVER_TIME_KEY = "server_time"

        val SERVER_TIME = "/$META_KEY/$SERVER_TIME_KEY"
    }

    object USER {

        fun getUser(uuid: String) = "/$USER_KEY/$uuid"
    }
}