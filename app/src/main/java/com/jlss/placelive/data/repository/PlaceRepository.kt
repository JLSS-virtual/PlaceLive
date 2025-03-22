package com.jlss.placelive.data.repository

import com.jlss.placelive.data.api.ApiService
import com.jlss.placelive.model.Place

class PlaceRepository(private val apiService: ApiService) {
    suspend fun getPlaces() = apiService.getPlaces()
    suspend fun addPlace(place: Place) = apiService.addPlace(place)
    suspend fun deletePlace(id: Int) = apiService.deletePlace(id)
}
