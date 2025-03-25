package com.jlss.placelive.ui.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.placelive.ui.navigation.Screen


// MainPlaceScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabsScreen(
    userId: Long,
    navController: NavController
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Places", "Geofences")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> PlaceScreen(
                userId = userId,
                navigateToGeofenceScreen = { placeId ->
                    navController.navigate(Screen.GeofenceScreen.createRoute(placeId))
                }
            )
            1 -> GeofenceListScreen(
                navigateToGeofenceDetail = { geofenceId ->
                    navController.navigate(Screen.GeofenceDetailScreen.createRoute(geofenceId))
                }
            )
        }
    }
}