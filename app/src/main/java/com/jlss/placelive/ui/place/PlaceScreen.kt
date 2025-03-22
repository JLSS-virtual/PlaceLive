package com.jlss.placelive.ui.place

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
import com.jlss.placelive.model.Place
import com.jlss.placelive.viewmodel.PlaceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceScreen() {
    val baseUrl = "http://192.168.63.201:8083/placelive-geofencing/v1/api/"


    val retrofitClient = remember { RetrofitClient(baseUrl) }  // ✅ Instance created dynamically
    val viewModel: PlaceViewModel = viewModel(factory = PlaceViewModel.Factory(retrofitClient.createApiService()))

    val placesList by viewModel.places.collectAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()

    // ✅ All fields included
    var placeName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var radius by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = placeName,
            onValueChange = { placeName = it },
            label = { Text("Place Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("Latitude") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = { Text("Longitude") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = radius,
            onValueChange = { radius = it },
            label = { Text("Radius") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val newPlace = Place(
                    name = placeName,
                    description = description,
                    latitude = latitude.toDoubleOrNull() ?: 0.0,
                    longitude = longitude.toDoubleOrNull() ?: 0.0,
                    radius = radius.toDoubleOrNull() ?: 0.0
                )

                coroutineScope.launch { viewModel.addPlace(newPlace) }

                // ✅ Clear fields after adding
                placeName = ""
                description = ""
                latitude = ""
                longitude = ""
                radius = ""
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text("Add Place")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(placesList) { place ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Name: ${place.name}")
                        Text(text = "Description: ${place.description}")
                        Text(text = "Lat: ${place.latitude}, Lon: ${place.longitude}")
                    }
                }
            }
        }
    }
}