package com.jlss.placelive.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jlss.placelive.data.api.RetrofitClient
import com.jlss.placelive.database.DatabaseInstance
import com.jlss.placelive.model.Geofence
import com.jlss.placelive.repository.GeofenceRepository
import com.jlss.placelive.utility.NetworkUtil
import com.jlss.placelive.viewmodel.GeofenceViewModel

import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeofenceScreen(placeId: Long) {
    val context = LocalContext.current
    val retrofitClient = remember { RetrofitClient() }
    val networkUtil = remember { NetworkUtil(context) }
    val viewModel: GeofenceViewModel = viewModel(
        factory = GeofenceViewModel.Factory(
            retrofitClient.createGeofenceApi(),
            GeofenceRepository(DatabaseInstance.getDatabase(context).geofenceDao()),
            networkUtil
        )
    )

    // Fetch geofences when screen loads or placeId changes
    LaunchedEffect(placeId) {
        viewModel.fetchGeofencesByPlaceId(placeId)
    }

    val geofences by viewModel.geofences.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    // Form fields
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("") }
    var isActive by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var notificationMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Input Fields
        GeofenceInputForm(
            latitude = latitude,
            onLatitudeChange = { latitude = it },
            longitude = longitude,
            onLongitudeChange = { longitude = it },
            radius = radius,
            onRadiusChange = { radius = it },
            isActive = isActive,
            onActiveChange = { isActive = it },
            notificationsEnabled = notificationsEnabled,
            onNotificationsChange = { notificationsEnabled = it },
            notificationMessage = notificationMessage,
            onMessageChange = { notificationMessage = it }
        )

        // Add Button
        Button(
            onClick = {
                val newGeofence = createGeofence(
                    latitude,
                    longitude,
                    radius,
                    isActive,
                    notificationsEnabled,
                    notificationMessage,
                    placeId
                )

                coroutineScope.launch {
                    viewModel.addGeofence(newGeofence,context)
                    // Reset form after successful addition
                    resetForm(
                        { latitude = it },
                        { longitude = it },
                        { radius = it },
                        { isActive = it },
                        { notificationsEnabled = it },
                        { notificationMessage = it }
                    )
                    // Refresh the list
                    viewModel.fetchGeofencesByPlaceId(placeId)
                }
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Create Geofence")
        }

        // Geofences List
        GeofenceList(
            geofences = geofences,
            onDelete = { viewModel.deleteGeofence(it,context) }
        )
    }
}

// Rest of the composable functions remain the same as in your original code
// (GeofenceInputForm, GeofenceList, GeofenceListItem, createGeofence, resetForm)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GeofenceInputForm(
    latitude: String,
    onLatitudeChange: (String) -> Unit,
    longitude: String,
    onLongitudeChange: (String) -> Unit,
    radius: String,
    onRadiusChange: (String) -> Unit,
    isActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    notificationMessage: String,
    onMessageChange: (String) -> Unit
) {
    Column {
        // Latitude Input
        OutlinedTextField(
            value = latitude,
            onValueChange = onLatitudeChange,
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )

        // Longitude Input
        OutlinedTextField(
            value = longitude,
            onValueChange = onLongitudeChange,
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )

        // Radius Input
        OutlinedTextField(
            value = radius,
            onValueChange = onRadiusChange,
            label = { Text("Radius (meters)") },
            modifier = Modifier.fillMaxWidth()
        )

        // Active Status Toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isActive,
                onCheckedChange = onActiveChange
            )
            Text("Active Geofence")
        }

        // Notifications Toggle
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsChange
            )
            Text("Enable Notifications")
        }

        // Notification Message
        if (notificationsEnabled) {
            OutlinedTextField(
                value = notificationMessage,
                onValueChange = onMessageChange,
                label = { Text("Notification Message") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GeofenceList(geofences: List<Geofence>, onDelete: (Long) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(geofences) { geofence ->
            GeofenceListItem(geofence, onDelete)
        }
    }
}

@Composable
private fun GeofenceListItem(geofence: Geofence, onDelete: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Lat: ${geofence.latitude}, Lon: ${geofence.longitude}")
            Text("Radius: ${geofence.radius}m")
            Text("Active: ${if (geofence.isActive) "Yes" else "No"}")
            Text("Notifications: ${if (geofence.notificationsEnabled) "Enabled" else "Disabled"}")
            geofence.notificationMessage?.let {
                Text("Message: $it")
            }
            Button(
                onClick = { onDelete(geofence.geofenceId) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        }
    }
}



private fun createGeofence(
    latitude: String,
    longitude: String,
    radius: String,
    isActive: Boolean,
    notificationsEnabled: Boolean,
    notificationMessage: String,
    placeId: Long
): Geofence {
    return Geofence(
        latitude = latitude.toDoubleOrNull() ?: 0.0,
        longitude = longitude.toDoubleOrNull() ?: 0.0,
        radius = radius.toDoubleOrNull() ?: 0.0,
        isActive = isActive,
        notificationsEnabled = notificationsEnabled,
        notificationMessage = notificationMessage.ifEmpty { null },
        placeId = placeId
    )
}

private fun resetForm(
    setLatitude: (String) -> Unit,
    setLongitude: (String) -> Unit,
    setRadius: (String) -> Unit,
    setIsActive: (Boolean) -> Unit,
    setNotificationsEnabled: (Boolean) -> Unit,
    setNotificationMessage: (String) -> Unit
) {
    setLatitude("")
    setLongitude("")
    setRadius("")
    setIsActive(true)
    setNotificationsEnabled(false)
    setNotificationMessage("")
}