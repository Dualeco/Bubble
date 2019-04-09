package com.kpiroom.bubble.source.api.impl.firebase.livedata

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.kpiroom.bubble.util.exceptions.db.DbEmptyFieldException

class FirebaseListLiveData<T>(
    ref: DatabaseReference,
    private val type: Class<T>
) : FirebaseLiveData<List<T>>(ref) {

    override fun onDataChangeListener(dataSnapshot: DataSnapshot): List<T> = dataSnapshot.children.map {
        (it.getValue(type) ?: throw DbEmptyFieldException())
    }
}