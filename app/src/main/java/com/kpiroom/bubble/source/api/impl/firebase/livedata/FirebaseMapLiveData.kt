package com.kpiroom.bubble.source.api.impl.firebase.livedata

import androidx.collection.ArrayMap
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.kpiroom.bubble.util.exceptions.db.DbEmptyFieldException

class FirebaseMapLiveData<T>(
    ref: DatabaseReference,
    private val type: Class<T>
) : FirebaseLiveData<ArrayMap<String, T>>(ref) {

    override fun onDataChangeListener(dataSnapshot: DataSnapshot): ArrayMap<String, T> =
        dataSnapshot.children.associateTo(ArrayMap()) {
            Pair(it.key ?: throw DbEmptyFieldException(), it.getValue(type) ?: throw DbEmptyFieldException())
        }
}