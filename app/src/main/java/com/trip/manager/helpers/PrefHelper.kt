package com.trip.manager.helpers

import android.content.SharedPreferences

const val LOGIN_STATUS_KEY = "LOGIN_STATUS"

class PrefHelper(private val prefs: SharedPreferences) {

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(LOGIN_STATUS_KEY, false)
        set(value) = prefs.edit().putBoolean(LOGIN_STATUS_KEY, value).apply()
}