package com.jlss.placelive.viewmodel.placeviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jlss.placelive.data.api.PlaceApi
import com.jlss.placelive.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaceViewModel(private val apiService: PlaceApi) : ViewModel() {

    // Holds the current list of places.
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> get() = _places

    // Load places when the ViewModel is created.
    init {
        fetchPlaces()
    }

    /**
     * Adds a new place via the API.
     * On success, appends the newly created place to the list.
     */
    // In PlaceViewModel.kt
    suspend fun addPlace(place: Place): Long? { // Return generated placeId
        return try {
            val response = apiService.addPlace(place)
            if (response.isSuccessful) {
                val newPlace = response.body()?.data
                newPlace?.let {
                    _places.value = _places.value + it
                    it.id // Return the server-generated ID
                }
            } else {
                println("Add place failed: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            println("Error adding place: ${e.message}")
            null
        }
    }

    /**
     * Deletes a place by its ID via the API.
     * On success, removes it from the local list.
     */
    fun deletePlace(placeId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deletePlace(placeId) // Response<ResponseDto<String>>
                if (response.isSuccessful) {
                    // Filter out the deleted place from the list.
                    _places.value = _places.value.filter { it.id?.toInt() != placeId }
                } else {
                    println("Delete place failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Error deleting place: ${e.message}")
            }
        }
    }

    /**
     * Fetches the latest list of places from the API and updates the local list.
     */
    private fun fetchPlaces() {
        viewModelScope.launch {
            try {
                val response = apiService.getPlaces() // Response<ResponseListDto<Place>>
                if (response.isSuccessful) {
                    val placesList = response.body()?.data ?: emptyList()
                    _places.value = placesList as List<Place>
                } else {
                    println("Error fetching places: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Error fetching places: ${e.message}")
            }
        }
    }

    /**
     * Custom Factory to pass PlaceApi when creating the ViewModel.
     */
    companion object {
        fun Factory(apiService: PlaceApi): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PlaceViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PlaceViewModel(apiService) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
