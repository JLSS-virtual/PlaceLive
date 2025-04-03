package com.jlss.placelive.ui.Screens.geofencescreeen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jlss.placelive.data.api.RetrofitClient
import com.jlss.placelive.data.repository.DataRepository
import com.jlss.placelive.database.DatabaseInstance
import com.jlss.placelive.repository.GeofenceRepository
import com.jlss.placelive.utility.NetworkUtil
import com.jlss.placelive.viewmodel.geofenceviewmodel.GeofenceViewModel

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
    // NEW: Create a DataRepository instance for full sync/update operations.
    val dataRepository = DataRepository(
        retrofitClient.createGeofenceApi(),
        geofenceDao
    )
    // Instantiate the GeofenceViewModel using a custom factory that supplies the dependencies
    val viewModel: GeofenceViewModel = viewModel(
        factory = GeofenceViewModel.Factory(apiService, repository,networkUtil,dataRepository)
    )

    // Observe loading state and selected geofence from the ViewModel
    val loadingState by viewModel.loadingState.collectAsState()
    val geofence by viewModel.selectedGeofenceDto.collectAsState()

    // Trigger fetch when the screen loads using the geofenceId and current context
    LaunchedEffect(geofenceId) {
        viewModel.getGeofenceById(geofenceId)
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        when (loadingState) {
            is GeofenceViewModel.GeofenceState.Loading -> {
                // Centered loading indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            is GeofenceViewModel.GeofenceState.Success -> {
                geofence?.let {
                    // Display details in a Card with padding, background, and spacing
                    Card(
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Geofence ID: ${it.geofenceId}",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Location: (${it.latitude}, ${it.longitude})",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Radius: ${it.radius} m",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Status: ${if (it.isActive) "Active" else "Inactive"}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Notifications: ${if (it.notificationsEnabled) "Enabled" else "Disabled"}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
            is GeofenceViewModel.GeofenceState.Error -> {
                // Centered error message with error color
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (loadingState as GeofenceViewModel.GeofenceState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}