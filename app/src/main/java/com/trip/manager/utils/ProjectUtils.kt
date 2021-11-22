package com.trip.manager.utils

import androidx.annotation.StringRes
import com.trip.manager.application.TMApplication

fun getStringResource(@StringRes res: Int) = TMApplication.appContext.getString(res)