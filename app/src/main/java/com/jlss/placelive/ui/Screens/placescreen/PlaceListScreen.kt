package com.jlss.placelive.ui.Screens.placescreen
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jlss.placelive.data.api.RetrofitClient

import com.jlss.placelive.viewmodel.placeviewmodel.PlaceViewModel

@Composable
fun PlaceListScreen(
    navigateBack: () -> Unit,
    navigateToGeofenceScreen: (Long) -> Unit,
) {
    val retrofitClient = remember { RetrofitClient() }
    val viewModel: PlaceViewModel =
        viewModel(factory = PlaceViewModel.Factory(retrofitClient.createPlaceApi()))

    val placesList by viewModel.places.collectAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Your Places",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(placesList) { place ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Name: ${place.name}")
                        Text(text = "id: ${place.id}")
                        Text(text = "Description: ${place.description}")
                        place.type?.let { Text(text = "Type: $it") }
                        if (place.tags.isNotEmpty()) {
                            Text(text = "Tags: ${place.tags.joinToString(", ")}")
                        }
                        Button(onClick = { place.id?.let { navigateToGeofenceScreen(it) } }) {
                            Text("Geofence")
                        }
                    }
                }
            }
        }

        Button(onClick = navigateBack, modifier = Modifier.padding(top = 8.dp)) {
            Text("Back")
        }
    }
}
