package com.jlss.placelive.ui.Screens.BarsOrTabs

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
import com.jlss.placelive.ui.Screens.placescreen.PlaceListScreen
import com.jlss.placelive.ui.Screens.placescreen.PlaceScreen


// BarPlaceScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTabsScreen(
    navController: NavController
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Places", "All Places")

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
                navigateToPlaceListScreen = { id ->
                    navController.navigate(Screen.PlaceListScreen.createRoute(id))
                }
            )
            1 -> PlaceListScreen(
                navigateBack = { navController.popBackStack() }, // Correct back navigation
                navigateToGeofenceScreen = { id ->
                    navController.navigate(Screen.GeofenceScreen.createRoute(id))
                }
            )
        }
    }
}