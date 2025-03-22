package com.jlss.placelive.model

data class Place(
    val placeId: Int? = null,
    val geofenceId: Double = 0.0,  // Default ID (will be assigned by backend)
    val name: String,              // Name input from UI
    val description: String,       // Description input from UI
    val latitude: Double,          // Latitude input
    val longitude: Double,         // Longitude input
    val radius: Double,            // Radius input
    var isActive: Boolean = true   // Default: Active when added
)
