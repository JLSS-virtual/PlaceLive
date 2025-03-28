package com.jlss.placelive.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jlss.placelive.data.api.GeofenceApi
import com.jlss.placelive.model.Geofence
import com.jlss.placelive.repository.GeofenceRepository
import com.jlss.placelive.utility.NetworkUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeofenceViewModel(
    private val apiService: GeofenceApi,
    private val repository: GeofenceRepository,
    private val networkUtil: NetworkUtil
) : ViewModel() {

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
            _loadingState.value = GeofenceState.Loading

            if (networkUtil.isConnected()) {
                try {
                    val response = apiService.getGeofencesByPlaceId(placeId)
                    if (response.isSuccessful) {
                        val geofencesList = response.body()?.data ?: emptyList()
                        _geofences.value = geofencesList
                    }
                } catch (e: Exception) {
                    _loadingState.value = GeofenceState.Error("Network error: ${e.message}")
                }
            } else {
                val cachedGeofences = repository.getAllGeofences()
                _geofences.value = cachedGeofences
            }
        }
    }
    fun fetchAllGeofence() {
        viewModelScope.launch {
            _loadingState.value = GeofenceState.Loading

            if (networkUtil.isConnected()) {
                try {
                    val response = apiService.getGeofence()
                    if (response.isSuccessful) {
                        val geofencesList = response.body()?.data ?: emptyList()

                        // Update LiveData
                        _geofences.value = geofencesList

                        // Update Room Database (Clear old data and insert new data)
                        repository.clearGeofences()
                        geofencesList.forEach { repository.addGeofence(it) }
                    }
                } catch (e: Exception) {
                    _loadingState.value = GeofenceState.Error("Network error: ${e.message}")
                }
            } else {
                // Fetch from local Room database when offline
                val cachedGeofences = repository.getAllGeofences()
                _geofences.value = cachedGeofences
            }
        }
    }


    fun getGeofenceById(geofenceId: Long) {
        viewModelScope.launch {
            _loadingState.value = GeofenceState.Loading

            if (networkUtil.isConnected()) {
                try {
                    val response = apiService.getGeofenceById(geofenceId)
                    if (response.isSuccessful) {
                        response.body()?.data?.let {
                            _selectedGeofence.value = it
                            _loadingState.value = GeofenceState.Success(it)
                            repository.addGeofence(it)
                        } ?: run {
                            _loadingState.value = GeofenceState.Error("Geofence not found")
                        }
                    } else {
                        _loadingState.value = GeofenceState.Error("Server error: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _loadingState.value = GeofenceState.Error("Network error: ${e.message}")
                }
            } else {
                val cachedGeofence = repository.getGeofence(geofenceId)
                if (cachedGeofence != null) {
                    _selectedGeofence.value = cachedGeofence
                    _loadingState.value = GeofenceState.Success(cachedGeofence)
                } else {
                    _loadingState.value = GeofenceState.Error("No offline data available")
                }
            }
        }
    }

    fun addGeofence(geofence: Geofence, context: Context) {
        viewModelScope.launch {
            if (networkUtil.isConnected()) {
                try {
                    val response = apiService.addGeofence(geofence)
                    if (response.isSuccessful) {
                        val newGeofence = response.body()?.data
                        if (newGeofence != null) {
                            _geofences.value = _geofences.value + newGeofence
                            repository.addGeofence(newGeofence)

                            // Show success message
                            Toast.makeText(context, "Geofence added successfully!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Show error message
                        Toast.makeText(context, "Failed to add geofence!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "No internet connection. Cannot add geofence.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun deleteGeofence(geofenceId: Long, context: Context) {
        viewModelScope.launch {
            if (networkUtil.isConnected()) {
                try {
                    val response = apiService.deleteGeofence(geofenceId.toInt())
                    if (response.isSuccessful) {
                        _geofences.value = _geofences.value.filter { it.geofenceId != geofenceId }
                        repository.removeGeofence(geofenceId.toInt())

                        // Show success message
                        Toast.makeText(context, "Geofence deleted successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Show error message
                        Toast.makeText(context, "Failed to delete geofence!", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "No internet connection. Cannot delete geofence.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    companion object {
        fun Factory(
            apiService: GeofenceApi,
            repository: GeofenceRepository,
            networkUtil: NetworkUtil
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(GeofenceViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return GeofenceViewModel(apiService, repository, networkUtil) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}