package com.example.placelive.ui.navigation

sealed class Screen(val route: String, val title: String? = null) {

    // Bottom Navigation Screens

    object Home : Screen("home", "Home")
    object Profile : Screen("profile", "Friend circle")
    object Messages : Screen("messages", "Messages")
    object Events : Screen("events", "Events")

    // Extended Navigation Screens
    object MainPlace : Screen("main_place","Place live")

    // Dynamic Navigation (With Parameters)
    object GeofenceScreen : Screen("geofence_screen/{placeId}") {
        fun createRoute(placeId: Long) = "geofence_screen/$placeId"
    }

    object GeofenceDetailScreen : Screen("geofence_detail_screen/{geofenceId}") {
        fun createRoute(geofenceId: Long) = "geofence_detail_screen/$geofenceId"
    }
}
