//package com.jlss.placelive.ui.Screens.geofencescreeen
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.jlss.placelive.data.api.RetrofitClient
//import com.jlss.placelive.data.repository.DataRepository
//import com.jlss.placelive.database.DatabaseInstance
//import com.jlss.placelive.repository.GeofenceRepository
//import com.jlss.placelive.utility.NetworkUtil
//import com.jlss.placelive.viewmodel.geofenceviewmodel.GeofenceViewModel
//
//@Composable
//fun GeofenceListScreen(
//    navigateToGeofenceDetail: (Long) -> Unit
//) {
//    val context = LocalContext.current
//    val retrofitClient = remember { RetrofitClient() }
//    val networkUtil = remember { NetworkUtil(context) }
//    val geofenceDao = DatabaseInstance.getDatabase(context).geofenceDao()
//    val dataRepository = DataRepository(
//        retrofitClient.createGeofenceApi(),
//        geofenceDao
//    )
//    val viewModel: GeofenceViewModel = viewModel(
//        factory = GeofenceViewModel.Factory(
//            apiService = retrofitClient.createGeofenceApi(),
//            repository = GeofenceRepository(DatabaseInstance.getDatabase(context).geofenceDao()),
//            networkUtil = networkUtil,
//            dataRepository = dataRepository
//        )
//    )
//
//    // Trigger geofence fetching when screen loads
//    LaunchedEffect(Unit) {
////        viewModel.updateGeofences()          ***NO NEED FOR THIS NOW AS WE ARE GOING TO NOT PRINT THIS LISTS WE JUST USE IT FOR A SPECIFIC USER.****
//       }
//
//    val geofences by viewModel.geofences.collectAsState(emptyList())
//
//    LazyColumn(modifier = Modifier.fillMaxSize()) {
//        items(geofences) { geofence ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = "Geofence ID: ${geofence.geofenceId}",
//                        style = MaterialTheme.typography.headlineSmall,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                    Text("Place ID: ${geofence.placeId}")
//                    Text("Radius: ${geofence.radius}m")
//                    Text("Location: (${geofence.latitude}, ${geofence.longitude})")
//                    Button(
//                        onClick = { navigateToGeofenceDetail(geofence.geofenceId) }
//                    ) {
//                        Text("View Details")
//                    }
//                }
//            }
//        }
//    }
//}