package com.kpiroom.bubble.source.api.impl.firebase.livedata

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.kpiroom.bubble.util.exceptions.db.DbEmptyFieldException

class FirebaseMapLiveData<T>(
    ref: DatabaseReference,
    private val type: Class<T>
) : FirebaseLiveData<Map<String, T>>(ref) {

    override fun onDataChangeListener(dataSnapshot: DataSnapshot): Map<String, T> = dataSnapshot.children.associate {
        Pair(it.key ?: throw DbEmptyFieldException(), it.getValue(type) ?: throw DbEmptyFieldException())
    }
}