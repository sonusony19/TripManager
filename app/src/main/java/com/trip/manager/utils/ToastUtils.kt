package com.trip.manager.utils

import android.widget.Toast
import androidx.annotation.StringRes
import com.trip.manager.application.TMApplication

fun showShortToast(message: String) {
    show(message, Toast.LENGTH_SHORT)
}

fun showShortToast(@StringRes resource: Int) {
    show(TMApplication.appContext.getString(resource), Toast.LENGTH_SHORT)
}

fun showLongToast(message: String) {
    show(message, Toast.LENGTH_LONG)
}

fun showLongToast(@StringRes resource: Int) {
    show(TMApplication.appContext.getString(resource), Toast.LENGTH_LONG)
}

private fun show(message: String, length: Int) {
    if (message.isEmpty()) return
    Toast.makeText(TMApplication.appContext, message, if (length == Toast.LENGTH_LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
}