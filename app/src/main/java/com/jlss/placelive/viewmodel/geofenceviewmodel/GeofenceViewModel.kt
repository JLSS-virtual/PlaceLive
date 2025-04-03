package com.jlss.placelive.viewmodel.geofenceviewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jlss.placelive.data.api.GeofenceApi
import com.jlss.placelive.data.repository.DataRepository      // NEW: Import for full sync operations
import com.jlss.placelive.model.GeofenceDto
import com.jlss.placelive.repository.GeofenceRepository
import com.jlss.placelive.utility.NetworkUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GeofenceViewModel(
    private val apiService: GeofenceApi,
    private val repository: GeofenceRepository,
    private val networkUtil: NetworkUtil,
    private val dataRepository: DataRepository                // NEW: Inject DataRepository for sync
) : ViewModel() {

    private val _geofences = MutableStateFlow<List<GeofenceDto>>(emptyList())
    val geofences: StateFlow<List<GeofenceDto>> get() = _geofences

    private val _selectedGeofenceDto = MutableStateFlow<GeofenceDto?>(null)
    val selectedGeofenceDto: StateFlow<GeofenceDto?> get() = _selectedGeofenceDto

    private val _loadingState = MutableStateFlow<GeofenceState>(GeofenceState.Loading)
    val loadingState: StateFlow<GeofenceState> get() = _loadingState

    sealed class GeofenceState {
        object Loading : GeofenceState()
        data class Success(val geofenceDto: GeofenceDto) : GeofenceState()
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
                _loadingState.value = GeofenceState.Error("No internet connection error")
            }
        }
    }

    /**
     * fetchAllGeofence() is used on the first app launch (or when a full sync is desired)
     * to fetch all data from the remote API and cache it locally.
     */


    /**
     * updateGeofences() can be used after the initial sync.
     * This method updates the local data without needing to perform a full fresh fetch.
     */



    fun getGeofenceById(geofenceId: Long) {
        viewModelScope.launch {
            _loadingState.value = GeofenceState.Loading

            if (networkUtil.isConnected()) {
                try {
                    val response = apiService.getGeofenceById(geofenceId)
                    if (response.isSuccessful) {
                        response.body()?.data?.let {
                            _selectedGeofenceDto.value = it
                            _loadingState.value = GeofenceState.Success(it)
                        } ?: run {
                            _loadingState.value = GeofenceState.Error("Geofence not found")
                        }
                    } else {
                        _loadingState.value =
                            GeofenceState.Error("Server error: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _loadingState.value = GeofenceState.Error("Network error: ${e.message}")
                }
            } else {
                _loadingState.value = GeofenceState.Error("No internet connection error")
            }
        }
    }

    fun addGeofence(geofenceDto: GeofenceDto, context: Context) {
        viewModelScope.launch {
            if (networkUtil.isConnected()) {
                try {
                    val response = apiService.addGeofence(geofenceDto)
                    if (response.isSuccessful) {
                        val newGeofence = response.body()?.data
                        if (newGeofence != null) {
                            _geofences.value = _geofences.value + newGeofence

                            Toast.makeText(context, "Geofence added successfully!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
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

                        Toast.makeText(context, "Geofence deleted successfully!", Toast.LENGTH_SHORT).show()
                    } else {
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
            networkUtil: NetworkUtil,
            dataRepository: DataRepository            // NEW: Added to factory for sync operations
        ): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(GeofenceViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return GeofenceViewModel(apiService, repository, networkUtil, dataRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}
