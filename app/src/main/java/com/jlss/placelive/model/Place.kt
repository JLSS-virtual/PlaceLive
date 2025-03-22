package com.jlss.placelive.model

data class Place(
    val placeId: Int? = null,
    val name: String,              // Name input from UI
    val description: String,       // Description input from UI
    val latitude: Double,          // Latitude input
    val longitude: Double,         // Longitude input

)
