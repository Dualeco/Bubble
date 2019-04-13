package com.kpiroom.bubble.source.api.impl.firebase.livedata

import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

abstract class FirebaseLiveData<T>(private val ref: DatabaseReference) : LiveData<T>() {

    abstract fun onDataChangeListener(dataSnapshot: DataSnapshot): T

    private lateinit var listener: ValueEventListener

    override fun onActive() {
        super.onActive()

        listener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                value = onDataChangeListener(snapshot)
            }
        }

        ref.addValueEventListener(listener)

    }

    override fun onInactive() {
        super.onInactive()
        ref.removeEventListener(listener)
    }
}