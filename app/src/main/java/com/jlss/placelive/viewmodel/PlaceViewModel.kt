package com.jlss.placelive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jlss.placelive.data.api.ApiService
import com.jlss.placelive.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaceViewModel(private val apiService: ApiService) : ViewModel() {

    // Holds the current list of places
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> get() = _places

    // Load places as soon as the ViewModel is created
    init {
        fetchPlaces()
    }

    /**
     * Add a new place by calling the API.
     * If successful, we update our local list to include the new place.
     */
    fun addPlace(place: Place) {
        viewModelScope.launch {
            try {
                val response = apiService.addPlace(place)
                if (response.isSuccessful && response.body() != null) {
                    // The server returns the newly created place, so we append it to our list
                    _places.value = _places.value + response.body()!!
                } else {
                    println("Add place failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Error adding place: ${e.message}")
            }
        }
    }

    /**
     * Delete a place by ID from the server.
     * If successful, we remove it from our local list.
     */
    fun deletePlace(placeId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.deletePlace(placeId)
                if (response.isSuccessful) {
                    // Filter out the deleted place from our local list
                    _places.value = _places.value.filter { it.placeId?.toInt() != placeId }
                } else {
                    println("Delete place failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Error deleting place: ${e.message}")
            }
        }
    }

    /**
     * Fetches the latest list of places from the server and updates our local list.
     */
    private fun fetchPlaces() {
        viewModelScope.launch {
            try {
                val response = apiService.getPlaces()
                _places.value = response.data!!
            } catch (e: Exception) {
                println("Error fetching places: ${e.message}")
            }
        }
    }

    /**
     * A custom Factory so we can pass `ApiService` when creating the ViewModel.
     */
    companion object {
        fun Factory(apiService: ApiService): ViewModelProvider.Factory {
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
