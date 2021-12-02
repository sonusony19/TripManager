package com.trip.manager.utils

import android.content.Context
import com.trip.manager.R
import java.text.SimpleDateFormat
import java.util.*

const val monthFormat = "MMM, yyyy"
const val appDateFormat = "d MMM, yyyy"
const val appTimeFormat = "hh:mm a"
const val appDateTime = "hh:mm a | d MMM, yyyy"
const val backendDateFormat = "yyyy-MM-dd"
const val backendDateTime = "yyyy-MM-dd HH:mm:ss"

fun timeStampToDate(timeStamp: Long?, fromBackend: Boolean = false): Date {
    val calendar = Calendar.getInstance()
    return if (timeStamp == null) calendar.time
    else {
        calendar.timeInMillis = if (fromBackend) timeStamp * 1000 else timeStamp
        calendar.time
    }
}

fun getFormattedDateTime(date: Date?, format: String, forBackend: Boolean = false): String {
    return if (date == null) "-"
    else SimpleDateFormat(format, if (forBackend) Locale.US else Locale.ENGLISH).format(date)
}

fun getFormattedDateTime(timeStamp: Long?, format: String, forBackend: Boolean = false, fromBackend: Boolean = false): String {
    return if (timeStamp == null) "-"
    else {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = if (fromBackend) timeStamp * 1000 else timeStamp
        SimpleDateFormat(format, if (forBackend) Locale.US else Locale.ENGLISH).format(calendar.time)
    }
}

fun getFormattedDate(date: Date?, forBackend: Boolean = false): String {
    return if (date == null) "-"
    else SimpleDateFormat(if (forBackend) backendDateFormat else appDateFormat, if (forBackend) Locale.US else Locale.ENGLISH).format(date)
}

fun getFormattedDate(timeStamp: Long?, forBackend: Boolean = false, fromBackend: Boolean = false): String {
    return if (timeStamp == null) ""
    else {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = if (fromBackend) timeStamp * 1000 else timeStamp
        SimpleDateFormat(if (forBackend) backendDateFormat else appDateFormat, if (forBackend) Locale.US else Locale.ENGLISH).format(calendar.time)
    }
}

fun getFormattedTime(time: Date?, forBackend: Boolean = false): String {
    return if (time == null) "-"
    else SimpleDateFormat(appTimeFormat, if (forBackend) Locale.US else Locale.ENGLISH).format(time)
}

fun getFormattedTime(timeStamp: Long?, forBackend: Boolean = false, fromBackend: Boolean = false): String {
    return if (timeStamp == null) "-"
    else {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = if (fromBackend) timeStamp * 1000 else timeStamp
        SimpleDateFormat(appTimeFormat, if (forBackend) Locale.US else Locale.ENGLISH).format(calendar.time)
    }
}

fun getMergedDateTime(date: Date?, time: Date?, format: String, forBackend: Boolean = false): String {
    if (date == null || time == null) return "-"
    val calendar = Calendar.getInstance(Locale.ENGLISH)
    calendar.timeInMillis = date.time
    val common = Calendar.getInstance(Locale.ENGLISH)
    common.timeInMillis = time.time
    calendar[Calendar.HOUR_OF_DAY] = common[Calendar.HOUR_OF_DAY]
    calendar[Calendar.MINUTE] = common[Calendar.MINUTE]
    return SimpleDateFormat(format, if (forBackend) Locale.US else Locale.ENGLISH).format(calendar.time)
}

fun Context.convertTimeToDuration(minutes: Int): String {
    return if (minutes == 0) "-"
    else {
        var duration = ""
        duration = when {
            minutes < 60 -> String.format(Locale.ENGLISH, getString(R.string._minutes), minutes)
            minutes == 60 -> String.format(Locale.ENGLISH, getString(R.string._hour), 1)
            else -> String.format(Locale.ENGLISH, getString(R.string._hour_minutes), minutes / 60, minutes % 60)
        }
        return duration
    }
}