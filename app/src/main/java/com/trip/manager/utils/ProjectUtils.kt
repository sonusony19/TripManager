package com.trip.manager.utils

import android.os.Build
import androidx.annotation.StringRes
import com.trip.manager.application.TMApplication
import java.util.*

fun getStringResource(@StringRes res: Int) = TMApplication.appContext.getString(res)

fun getDeviceModel(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.lowercase(Locale.getDefault()).startsWith(manufacturer.lowercase(Locale.getDefault()))) {
        model.titleCase()
    } else {
        manufacturer.titleCase() + " " + model
    }
}