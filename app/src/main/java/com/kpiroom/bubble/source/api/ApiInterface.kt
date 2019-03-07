package com.kpiroom.bubble.source.api

interface ApiInterface {

    suspend fun getServerVersion(): String

    suspend fun setServerVersion(version: String)

    suspend fun signUp(email: String, password: String): String?
    suspend fun signIn(email: String, password: String): String?

    suspend fun sendPasswordResetEmail(email: String)
}