package com.jlss.placelive.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.placelive.ui.navigation.Screen
import com.jlss.placelive.ui.Screens.GeofenceDetailScreen
import com.jlss.placelive.ui.Screens.GeofenceScreen
import com.jlss.placelive.ui.Screens.MainTabsScreen
import androidx.compose.foundation.layout.padding
import com.example.placelive.ui.screens.HomeScreen
import com.jlss.placelive.ui.Components.CustomTopBar
import com.jlss.placelive.ui.Screens.EventsScreen
import com.jlss.placelive.ui.Screens.MessageScreen
import com.jlss.placelive.ui.Screens.ProfileScreen



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(toggleTheme: (Boolean) -> Unit, isDarkMode: Boolean) {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            CustomTopBar(
                onSettingsClick = { /* Handle settings click, e.g., navigate to a settings screen */ },
                onSearchClick = {
                    // Handle search click, e.g., open a search dialog or navigate to a search screen
                },
                onProfileClick = { /* Handle profile click, e.g., navigate to profile screen */ }
            ) { toggleTheme(!isDarkMode) } // Add Theme Toggle

        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.MainPlace.route,
            modifier = Modifier.padding(innerPadding)  // Use the innerPadding here
        ) {
            // Dedicated Profile screen
            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            // Dedicated Message screen
            composable(Screen.Messages.route) {
                MessageScreen()
            }
            composable(Screen.Home.route) {
                HomeScreen()
            }
            composable(Screen.Events.route) {
                EventsScreen()
            }
            composable(Screen.MainPlace.route) {
                MainTabsScreen(
                    userId = 123,
                    navController = navController
                )
            }
            composable(
                route = Screen.GeofenceScreen.route,
                arguments = listOf(navArgument("placeId") { type = NavType.LongType })
            ) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getLong("placeId") ?: 0L
                GeofenceScreen(placeId = placeId)
            }
            composable(
                route = Screen.GeofenceDetailScreen.route,
                arguments = listOf(navArgument("geofenceId") { type = NavType.LongType })
            ) { backStackEntry ->
                val geofenceId = backStackEntry.arguments?.getLong("geofenceId") ?: 0L
                GeofenceDetailScreen(geofenceId = geofenceId)
            }
        }
    }
}
