package com.jlss.placelive.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jlss.placelive.data.api.GeofenceApi
import com.jlss.placelive.model.Geofence
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeofenceViewModel(private val apiService: GeofenceApi) : ViewModel() {

    private val _geofences = MutableStateFlow<List<Geofence>>(emptyList())
    val geofences: StateFlow<List<Geofence>> get() = _geofences

    private val _selectedGeofence = MutableStateFlow<Geofence?>(null)
    val selectedGeofence: StateFlow<Geofence?> get() = _selectedGeofence

    private val _loadingState = MutableStateFlow<GeofenceState>(GeofenceState.Loading)
    val loadingState: StateFlow<GeofenceState> get() = _loadingState

    sealed class GeofenceState {
        object Loading : GeofenceState()
        data class Success(val geofence: Geofence) : GeofenceState()
        data class Error(val message: String) : GeofenceState()
    }

    fun fetchGeofencesByPlaceId(placeId: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.getGeofencesByPlaceId(placeId)
                if (response.isSuccessful) {
                    _geofences.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                println("Error fetching geofences: ${e.message}")
            }
        }
    }

    fun getGeofenceById(geofenceId: Long) {
        viewModelScope.launch {
            _loadingState.value = GeofenceState.Loading
            try {
                val response = apiService.getGeofenceById(geofenceId)
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _selectedGeofence.value = it
                        _loadingState.value = GeofenceState.Success(it)
                    } ?: run {
                        _loadingState.value = GeofenceState.Error("Geofence not found")
                    }
                } else {
                    _loadingState.value = GeofenceState.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                _loadingState.value = GeofenceState.Error("Network error: ${e.message}")
            }
        }
    }


//    init {
//        fetchGeofences()
//    }

    fun addGeofence(geofence: Geofence) {
        viewModelScope.launch {
            try {
                val response = apiService.addGeofence(geofence)
                if (response.isSuccessful) {
                    val newGeofence = response.body()?.data
                    if (newGeofence != null) {
                        _geofences.value = _geofences.value + newGeofence
                    }
                }
            } catch (e: Exception) {
                println("Error adding geofence: ${e.message}")
            }
        }
    }

    fun deleteGeofence(geofenceId: Long) {
        viewModelScope.launch {
            try {
                val response = apiService.deleteGeofence(geofenceId.toInt())
                if (response.isSuccessful) {
                    _geofences.value = _geofences.value.filter { it.geofenceId != geofenceId }
                }
            } catch (e: Exception) {
                println("Error deleting geofence: ${e.message}")
            }
        }
    }

    fun fetchGeofences() {
        viewModelScope.launch {
            try {
                val response = apiService.getGeofence()
                if (response.isSuccessful) {
                    val geofencesList = response.body()?.data ?: emptyList()
                    _geofences.value = geofencesList
                }
            } catch (e: Exception) {
                println("Error fetching geofences: ${e.message}")
            }
        }
    }

    companion object {
        fun Factory(apiService: GeofenceApi): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(GeofenceViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return GeofenceViewModel(apiService) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}