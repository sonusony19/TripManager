package com.trip.manager.listeners

interface FirebaseDataListener<T> {
    fun onSuccess(result: T)
    fun onFailure(error: String)
}