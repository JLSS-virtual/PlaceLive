package com.jlss.placelive.data.dto

class TrackerDto {

    val isUserIn = false

    val geofenceId: Long = 0

    val placeId: Long =0

    val userId: Long = 0  // user id will be setted when fetching the tracker data from server.

    val latitude: Double = 0.0  // Added latitude

    val longitude: Double = 0.0

    val radius: Double = 0.0

}