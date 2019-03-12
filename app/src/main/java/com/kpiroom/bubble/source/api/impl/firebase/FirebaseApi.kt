package com.kpiroom.bubble.source.api.impl.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kpiroom.bubble.source.api.ApiInterface

class FirebaseApi : ApiInterface {

    private val dbUtil = FirebaseDbUtil(FirebaseDatabase.getInstance().apply {
        setPersistenceEnabled(false)
    })

    private val authUtil = FirebaseAuthUtil(FirebaseAuth.getInstance())

    override suspend fun getServerVersion(): String = dbUtil.read(FirebaseStructure.VERSION, String::class.java)

    override suspend fun setServerVersion(version: String) = dbUtil.write(FirebaseStructure.VERSION, version)

    override suspend fun signUp(email: String, password: String) = authUtil.signUp(email, password).also {
        it?.let { id ->
            dbUtil.write("${FirebaseStructure.USER(id)}", id)
        }
    }

    override suspend fun signIn(email: String, password: String): String? = authUtil.signIn(email, password)

    override suspend fun sendPasswordResetEmail(email: String) = authUtil.sendPasswordResetEmail(email)
}