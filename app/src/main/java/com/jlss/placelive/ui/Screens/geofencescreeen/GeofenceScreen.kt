package com.jlss.placelive.ui.Screens.geofencescreeen

import androidx.compose.foundation.background
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
import com.jlss.placelive.data.repository.DataRepository
import com.jlss.placelive.database.DatabaseInstance
import com.jlss.placelive.model.GeofenceDto
import com.jlss.placelive.repository.GeofenceRepository
import com.jlss.placelive.utility.NetworkUtil
import com.jlss.placelive.viewmodel.geofenceviewmodel.GeofenceViewModel

import kotlinx.coroutines.launch


@Composable
fun GeofenceScreen(
    placeId: Long,
    navigateToGeofenceDetail: (Long) -> Unit
) {
    val context = LocalContext.current
    val retrofitClient = remember { RetrofitClient() }
    val networkUtil = remember { NetworkUtil(context) }
    val geofenceDao = DatabaseInstance.getDatabase(context).geofenceDao()
    // Create a DataRepository instance for full sync/update operations.
    val dataRepository = DataRepository(
        retrofitClient.createGeofenceApi(),
        geofenceDao
    )
    val viewModel: GeofenceViewModel = viewModel(
        factory = GeofenceViewModel.Factory(
            retrofitClient.createGeofenceApi(),
            GeofenceRepository(DatabaseInstance.getDatabase(context).geofenceDao()),
            networkUtil,
            dataRepository
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
        // Heading for the input section with background and padding
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(12.dp)
        ) {
            Text(
                text = "Add New Geofences to this Place $placeId",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Input Fields for Geofence Data
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
        Spacer(modifier = Modifier.height(12.dp))

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
                    if (newGeofence != null) {
                        viewModel.addGeofence(newGeofence, context)
                    }
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
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp)
        ) {
            Text("Create Geofence")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Heading for the list section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(12.dp)
        ) {
            Text(
                text = "Live Geofences on this Place",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Geofences List
        GeofenceList(
            geofenceDtos = geofences,
            onDelete = { viewModel.deleteGeofence(it, context) } ,
                    navigateToGeofenceDetail=navigateToGeofenceDetail
        )
    }
}

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
        OutlinedTextField(
            value = latitude,
            onValueChange = onLatitudeChange,
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = longitude,
            onValueChange = onLongitudeChange,
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = radius,
            onValueChange = onRadiusChange,
            label = { Text("Radius (meters)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isActive,
                onCheckedChange = onActiveChange
            )
            Text("Active Geofence")
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsChange
            )
            Text("Enable Notifications")
        }
        if (notificationsEnabled) {
            Spacer(modifier = Modifier.height(8.dp))
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
private fun GeofenceList(geofenceDtos: List<GeofenceDto>, onDelete: (Long) -> Unit,
                         navigateToGeofenceDetail: (Long) -> Unit ) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(geofenceDtos) { geofence ->
            GeofenceListItem(geofence, onDelete,navigateToGeofenceDetail)
        }
    }
}

@Composable
private fun GeofenceListItem(geofenceDto: GeofenceDto, onDelete: (Long) -> Unit,
                             navigateToGeofenceDetail: (Long) -> Unit) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(16.dp)
        ) {
            Text(
                text = "Lat: ${geofenceDto.latitude}, Lon: ${geofenceDto.longitude}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Radius: ${geofenceDto.radius}m",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Status: ${if (geofenceDto.isActive) "Active" else "Inactive"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Notifications: ${if (geofenceDto.notificationsEnabled) "Enabled" else "Disabled"}",
                style = MaterialTheme.typography.bodyMedium
            )
            geofenceDto.notificationMessage?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Message: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            // Subheading for the card's action
            Text(
                text = "Manage this geofence",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Button(
                onClick = { onDelete(geofenceDto.geofenceId) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Delete")
            }
            Button(
                onClick = { navigateToGeofenceDetail(geofenceDto.geofenceId) }
            ) {
                Text("View Details")
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
): GeofenceDto? {
    return notificationMessage.ifEmpty { null }?.let {
        GeofenceDto(
        latitude = latitude.toDoubleOrNull() ?: 0.0,
        longitude = longitude.toDoubleOrNull() ?: 0.0,
        radius = radius.toDoubleOrNull() ?: 0.0,
        isActive = isActive,
        notificationsEnabled = notificationsEnabled,
        notificationMessage = it,
        placeId = placeId
    )
    }
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
