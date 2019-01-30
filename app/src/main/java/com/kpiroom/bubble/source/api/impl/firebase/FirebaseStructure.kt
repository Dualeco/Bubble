package com.kpiroom.bubble.source.api.impl.firebase

object FirebaseStructure {

    private val VERSION_KEY = "version"
    private val META_KEY = "meta"

    val VERSION = "/$VERSION_KEY"

    object META { // E. g.

        private val SERVER_TIME_KEY = "server_time"

        val SERVER_TIME = "/$META_KEY/$SERVER_TIME_KEY"
    }
}