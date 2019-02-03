package com.kpiroom.bubble.source.api.impl.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kpiroom.bubble.source.api.ApiInterface

class FirebaseApi : ApiInterface {

    private val dbUtil = FirebaseDbUtil(FirebaseDatabase.getInstance().apply {
        setPersistenceEnabled(true)
    })

    val auth = FirebaseAuth.getInstance()
    private val authUtil = FirebaseAuthUtil(auth)

    override suspend fun getServerVersion(): String = dbUtil.read(FirebaseStructure.VERSION, String::class.java)

    override suspend fun setServerVersion(version: String) = dbUtil.write(FirebaseStructure.VERSION, version)

    suspend fun signUp(email: String, password: String): String? {
        val id = authUtil.signUp(email, password)
        if (id != null) {
            dbUtil.write("user/$id", id)
        }
        return id
    }

    suspend fun signIn(email: String, password: String) = authUtil.signIn(email, password)

    suspend fun doesEmailExist(email: String) = authUtil.doesEmailExist(email)

    suspend fun sendPasswordResetEmail(email: String) = authUtil.sendPasswordResetEmail(email)
}