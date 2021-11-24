package com.trip.manager.baseclasses


data class Response<T>(
        val success: Boolean = true,
        val error: String? = null,
        val data: T? = null
)
