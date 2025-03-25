package com.jlss.placelive.data.repository

import com.jlss.placelive.data.api.PlaceApi
import com.jlss.placelive.model.Place

class PlaceRepository(private val apiService: PlaceApi) {
    suspend fun getPlaces() = apiService.getPlaces()
    suspend fun addPlace(place: Place) = apiService.addPlace(place)
    suspend fun deletePlace(id: Int) = apiService.deletePlace(id)
}
