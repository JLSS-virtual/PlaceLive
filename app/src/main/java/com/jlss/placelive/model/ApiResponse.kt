package com.jlss.placelive.model

import com.jlss.placelive.data.api.GeofenceDto

data class ApiResponse(
    val success: Boolean,
    val data: List<GeofenceDto>?
)
