package com.jlss.placelive.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.placelive.ui.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Place

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    // List of bottom nav items with associated icons
    val items = listOf(
        Screen.Home to Icons.Filled.Home,
        Screen.MainPlace to Icons.Filled.Place,
        Screen.Messages to Icons.Filled.Message,
        Screen.Events to Icons.Filled.Event,
        Screen.Profile to Icons.Filled.Person
    )

    // Observe the current route
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        items.forEach { (screen, icon) ->
            NavigationBarItem(
                label = { screen.title?.let { Text(it) } },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                icon = { Icon(imageVector = icon, contentDescription = screen.title) }
            )
        }
    }
}
