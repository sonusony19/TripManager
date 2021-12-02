package com.trip.manager.baseclasses

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.trip.manager.listeners.FirebaseDataListener
import com.trip.manager.utils.databaseError

open class BaseRepository {

    var databaseAndListener = arrayListOf<Pair<DatabaseReference, ValueEventListener>>()

    fun setObject(database: DatabaseReference, data: Any, firebaseDataListener: FirebaseDataListener<String>? = null) {
        database.setValue(data).addOnCompleteListener {
            if (it.isSuccessful) firebaseDataListener?.onSuccess(database.key ?: "")
            else firebaseDataListener?.onFailure(it.exception?.localizedMessage ?: databaseError)
        }
    }

    fun updateObject(database: DatabaseReference, data: Map<String, Any>, firebaseDataListener: FirebaseDataListener<String>? = null) {
        database.updateChildren(data).addOnCompleteListener {
            if (it.isSuccessful) firebaseDataListener?.onSuccess(database.key ?: "")
            else firebaseDataListener?.onFailure(it.exception?.localizedMessage ?: databaseError)
        }
    }

    fun removeObject(database: DatabaseReference, listener: FirebaseDataListener<Any>?) {
        database.removeValue().addOnCompleteListener {
            if (it.isSuccessful) listener?.onSuccess(Any())
            else listener?.onFailure(it.exception?.localizedMessage ?: databaseError)
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
            databaseAndListener.add(Pair(database, databaseListener))
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
            databaseAndListener.add(Pair(database, databaseListener))
        }
    }

    fun removeListener() {
        databaseAndListener.forEach { it.first.removeEventListener(it.second) }
        databaseAndListener.clear()
    }

}