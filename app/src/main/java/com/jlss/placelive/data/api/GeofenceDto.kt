package com.jlss.placelive.data.api

data class GeofenceDto(
    val geofenceId: Long,
    val latitude: Double,
    val longitude: Double,
    val radius: Double
)
