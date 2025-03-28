package com.jlss.placelive.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jlss.placelive.data.api.RetrofitClient
import com.jlss.placelive.database.DatabaseInstance
import com.jlss.placelive.repository.GeofenceRepository
import com.jlss.placelive.utility.NetworkUtil
import com.jlss.placelive.viewmodel.GeofenceViewModel

/**
 * ## GeofenceDetailScreen
 *
 * **Purpose:**
 * Displays the details of a specific geofence. It first tries to fetch the data
 * from the remote API. If there is no internet connection, it falls back to the locally
 * cached data stored in Room.
 *
 * **Core Logic:**
 * - Retrieves the current `Context` using `LocalContext.current`.
 * - Initializes the database instance, DAO, and repository from Room.
 * - Creates the API service using Retrofit.
 * - Instantiates the GeofenceViewModel with a custom Factory that provides the API service and repository.
 * - Triggers a fetch for the geofence details when the screen loads, using the provided geofenceId.
 *
 * **Usage:**
 * Call this composable and pass the geofenceId to display details.
 *
 * @param geofenceId The ID of the geofence to display.
 */
@Composable
fun GeofenceDetailScreen(geofenceId: Long) {
    // Get the current context
    val context = LocalContext.current

    // Initialize the Room database, DAO, and repository
    val database = DatabaseInstance.getDatabase(context)
    val geofenceDao = database.geofenceDao()
    val repository = GeofenceRepository(geofenceDao)

    // Create the API service from Retrofit
    val retrofitClient = RetrofitClient()
    val apiService = retrofitClient.createGeofenceApi()
    val networkUtil = NetworkUtil(context)

    // Instantiate the GeofenceViewModel using a custom factory that supplies the dependencies
    val viewModel: GeofenceViewModel = viewModel(
        factory = GeofenceViewModel.Factory(apiService, repository,networkUtil)
    )

    // Observe loading state and selected geofence from the ViewModel
    val loadingState by viewModel.loadingState.collectAsState()
    val geofence by viewModel.selectedGeofence.collectAsState()

    // Trigger fetch when the screen loads using the geofenceId and current context
    LaunchedEffect(geofenceId) {
        viewModel.getGeofenceById(geofenceId)
    }

    // UI layout: Display the geofence details or loading/error states
    Column(modifier = Modifier.padding(16.dp)) {
        when (loadingState) {
            is GeofenceViewModel.GeofenceState.Loading -> {
                CircularProgressIndicator()
            }
            is GeofenceViewModel.GeofenceState.Success -> {
                geofence?.let {
                    Text("Geofence ID: ${it.geofenceId}", style = MaterialTheme.typography.headlineSmall)
                    Text("Location: (${it.latitude}, ${it.longitude})")
                    Text("Radius: ${it.radius}m")
                    Text("Status: ${if (it.isActive) "Active" else "Inactive"}")
                    Text("Notifications: ${if (it.notificationsEnabled) "Enabled" else "Disabled"}")
                }
            }
            is GeofenceViewModel.GeofenceState.Error -> {
                Text(
                    text = (loadingState as GeofenceViewModel.GeofenceState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
