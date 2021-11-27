package com.trip.manager.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.trip.manager.R
import com.trip.manager.application.TMApplication

@SuppressLint("ResourceType")
fun ImageView.setGif(@RawRes gif: Int) {
    Glide.with(TMApplication.appContext).asGif().load(R.raw.out_of_office).apply(RequestOptions().placeholder(R.raw.out_of_office).error(R.raw.out_of_office)).into(this)
}