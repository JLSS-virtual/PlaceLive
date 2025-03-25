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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jlss.placelive.data.api.RetrofitClient
import com.jlss.placelive.viewmodel.GeofenceViewModel

// GeofenceDetailScreen.kt
@Composable
fun GeofenceDetailScreen(geofenceId: Long) {
    val viewModel: GeofenceViewModel = viewModel(
        factory = GeofenceViewModel.Factory(
            RetrofitClient().createGeofenceApi()
        )
    )
    val loadingState by viewModel.loadingState.collectAsState()
    val geofence by viewModel.selectedGeofence.collectAsState()

    // Trigger fetch when screen loads
    LaunchedEffect(geofenceId) {
        viewModel.getGeofenceById(geofenceId)
    }

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