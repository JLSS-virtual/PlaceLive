package com.jlss.placelive.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jlss.placelive.data.api.RetrofitClient
import com.jlss.placelive.viewmodel.GeofenceViewModel

// GeofenceListScreen.kt
@Composable
fun GeofenceListScreen(
    viewModel: GeofenceViewModel = viewModel( // Add proper initialization
        factory = GeofenceViewModel.Factory(
            RetrofitClient().createGeofenceApi()
        )
    ),
    navigateToGeofenceDetail: (Long) -> Unit
) {
    viewModel.fetchGeofences()
    val geofences by viewModel.geofences.collectAsState(emptyList())

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(geofences) { geofence ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Geofence ID: ${geofence.geofenceId}")
                    Text("Place ID: ${geofence.placeId}")
                    Text("Radius: ${geofence.radius}m")
                    Text("Location: (${geofence.latitude}, ${geofence.longitude})")
                    Button(
                        onClick = { navigateToGeofenceDetail(geofence.geofenceId) }
                    ) {
                        Text("View Details")
                    }
                }
            }
        }
    }
}