package com.trip.manager.utils

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trip.manager.R
import com.trip.manager.application.TMApplication
import java.util.*

@SuppressLint("ResourceType")
fun ImageView.setGif(@RawRes gif: Int) {
    Glide.with(TMApplication.appContext).asGif().load(R.raw.out_of_office).apply(RequestOptions().placeholder(R.raw.out_of_office).error(R.raw.out_of_office)).into(this)
}

fun String.titleCase(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

@BindingAdapter("setUserImage")
fun ImageView.setUserImage(imageUri: Uri?) {
    Glide.with(TMApplication.appContext).load(imageUri).apply(RequestOptions().error(R.drawable.user_avatar).placeholder(R.drawable.user_avatar)).into(this)
}

@BindingAdapter("setUserImage")
fun ImageView.setUserImage(imageUri: String?) {
    Glide.with(TMApplication.appContext).load(imageUri).apply(RequestOptions().error(R.drawable.user_avatar).placeholder(R.drawable.user_avatar)).into(this)
}

@BindingAdapter("setGreetings")
fun TextView.setGreetings(name: String?) {
    text = String.format(getStringResource(R.string.hii_name), name ?: "")
}

@BindingAdapter("setDate")
fun TextView.setDate(timestamp: Long?) {
    text = getFormattedDate(timestamp)
}

@BindingAdapter("setDateTime")
fun TextView.setDateTime(timestamp: Long?) {
    text = getFormattedDateTime(timestamp, format = appDateTime)
}