package com.trip.manager.baseclasses

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.utils.databaseError

open class BaseRepository {

    var databaseAndListener = hashMapOf<DatabaseReference, ValueEventListener>()

    fun setObject(database: DatabaseReference, data: Any, firebaseDataListener: FirebaseDataListener<Any>) {
        database.setValue(data).addOnCompleteListener {
            if (it.isSuccessful) firebaseDataListener.onSuccess(true)
            else firebaseDataListener.onFailure(it.exception?.localizedMessage ?: databaseError)
        }
    }


    inline fun <reified T> listenToObject(database: DatabaseReference, model: Class<*>, firebaseDataListener: FirebaseDataListener<T>, once: Boolean = false) {
        val databaseListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(model)?.let {
                    if (it is T) firebaseDataListener.onSuccess(it)
                    else firebaseDataListener.onFailure(databaseError)
                } ?: firebaseDataListener.onFailure(databaseError)
            }

            override fun onCancelled(error: DatabaseError) {
                firebaseDataListener.onFailure(error.message)
            }
        }
        if (once) database.addListenerForSingleValueEvent(databaseListener)
        else {
            database.addValueEventListener(databaseListener)
            databaseAndListener[database] = databaseListener
        }
    }

    inline fun <reified T> listenToList(database: DatabaseReference, model: Class<*>, firebaseDataListener: FirebaseDataListener<List<T>>, once: Boolean = false) {
        val databaseListener = object : ValueEventListener {
            override fun onDataChange(parentSnapshot: DataSnapshot) {
                val temp = arrayListOf<T>()
                parentSnapshot.children.forEach { snapshot ->
                    snapshot.getValue(model)?.let { if (it is T) temp.add(it) }
                }
                firebaseDataListener.onSuccess(temp)
            }

            override fun onCancelled(error: DatabaseError) {
                firebaseDataListener.onFailure(error.message)
            }
        }
        if (once) database.addListenerForSingleValueEvent(databaseListener)
        else {
            database.addValueEventListener(databaseListener)
            databaseAndListener[database] = databaseListener
        }
    }

    fun removeListener() {
        databaseAndListener.keys.forEach { database ->
            databaseAndListener[database]?.let { it -> database.removeEventListener(it) }
        }
        databaseAndListener.clear()
    }

}