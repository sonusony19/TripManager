package com.trip.manager.ui.home.model

import com.trip.manager.ui.trip.model.Trip

data class Trips(
        val currentTrips: ArrayList<Trip> = arrayListOf(),
        val upcomingTrips: ArrayList<Trip> = arrayListOf(),
        val pastTrips: ArrayList<Trip> = arrayListOf(),
)