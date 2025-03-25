package com.jlss.placelive.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jlss.placelive.data.api.RetrofitClient
import com.jlss.placelive.model.Place
import com.jlss.placelive.viewmodel.PlaceViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PlaceScreen(
    userId: Long,
    navigateToGeofenceScreen: (Long) -> Unit // Lambda for navigation
) {
    val retrofitClient = remember { RetrofitClient() }
    val viewModel: PlaceViewModel =
        viewModel(factory = PlaceViewModel.Factory(retrofitClient.createPlaceApi()))

    val placesList by viewModel.places.collectAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()

    // Form fields
    var placeName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Dropdown for type
    val types = listOf("Park", "Restaurant", "Museum", "Cafe")
    var selectedType by remember { mutableStateOf(types[0]) }
    var expanded by remember { mutableStateOf(false) }

    // Chip input for tags
    var tags by remember { mutableStateOf(listOf<String>()) }
    var newTag by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Place Name Input
        OutlinedTextField(
            value = placeName,
            onValueChange = { placeName = it },
            label = { Text("Place Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Description Input
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        // Type Dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedType,
                onValueChange = { }, // No direct editing
                label = { Text("Type") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    }
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                types.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            selectedType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        // Tags Input (Chip-based)
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Tags", style = MaterialTheme.typography.labelMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = newTag,
                    onValueChange = { newTag = it },
                    label = { Text("Add Tag") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (newTag.isNotBlank()) {
                        tags = tags + newTag
                        newTag = ""
                    }
                }) {
                    Text("Add")
                }
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tags.forEach { tag ->
                    SuggestionChip(
                        onClick = { tags = tags - tag },
                        label = { Text(tag) }
                    )
                }
            }
        }

        // Add Button
        Button(
            onClick = {
                val newPlace = Place(
                    name = placeName,
                    description = description,
                    type = selectedType,
                    tags = tags,
                    ownerId = userId
                )

                coroutineScope.launch {
                    val placeId = viewModel.addPlace(newPlace) // Get generated ID

                    // Only clear fields if successful
                    if (placeId != null) {
                        placeName = ""
                        description = ""
                        selectedType = types[0]
                        tags = emptyList()
                    }
                }
            }
        ) {
            Text("Add Place")
        }

        // List of Places
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
                        place.type?.let {
                            Text(text = "Type: $it")
                        }
                        if (place.tags.isNotEmpty()) {
                            Text(text = "Tags: ${place.tags.joinToString(", ")}")
                        }
                        // In PlaceScreen.kt
                        Button(
                            onClick = {
                                place.id?.let {
                                    println("Navigating with placeId: $it")
                                    navigateToGeofenceScreen(it)
                                } ?: println("Error: Place ID is null")
                            }
                        ) {
                            Text("Add Geofence")
                        }
                    }
                }
            }
        }
    }
}