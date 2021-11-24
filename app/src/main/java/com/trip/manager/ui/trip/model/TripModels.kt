package com.trip.manager.ui.trip.model


data class Trip(
        var id: String = "",
        var name: String? = null,
        var members: List<String> = arrayListOf()
)

data class Essential(
        var id: String = "",
        var name: String = "",
        var createdAt: Long = 0L,
        var handledAt: Long = 0L,
        var updatedAt: Long = 0L,
        var responsible: String = "",
        var handledBy: String = ""
)