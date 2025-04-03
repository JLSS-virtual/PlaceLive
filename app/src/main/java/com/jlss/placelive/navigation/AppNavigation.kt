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
import com.jlss.placelive.ui.Screens.geofencescreeen.GeofenceDetailScreen
import com.jlss.placelive.ui.Screens.geofencescreeen.GeofenceScreen
import com.jlss.placelive.ui.Screens.BarsOrTabs.MainTabsScreen
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.placelive.ui.screens.HomeScreen
import com.jlss.placelive.ui.Components.CustomTopBar
import com.jlss.placelive.ui.Screens.ContactListScreen
import com.jlss.placelive.ui.Screens.extrascreens.EventsScreen
import com.jlss.placelive.ui.Screens.messagescreen.MessageScreen
import com.jlss.placelive.ui.Screens.placescreen.PlaceListScreen
import com.jlss.placelive.ui.Screens.profilescreen.ProfileScreen
import com.jlss.placelive.ui.Screens.registrationscreen.LogOut
import com.jlss.placelive.ui.Screens.registrationscreen.LoginScreen
import com.jlss.placelive.ui.Screens.registrationscreen.RegistrationScreen
import com.jlss.placelive.ui.Screens.welcomescreen.WelcomeScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (isLoggedIn) Screen.Home.route else Screen.WelcomeScreen.route
    // Check if current destination is WelcomeScreen
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    // only hiding all this fields for starting screens .
    val isWelcomeScreen = currentDestination?.route == Screen.WelcomeScreen.route
    val isLoginScreen = currentDestination?.route == Screen.LoginScreen.route
    val isRegistrationScreen = currentDestination?.route == Screen.RegistrationScreen.route
    Scaffold(
        topBar = {
            if (!isWelcomeScreen&&!isLoginScreen&&!isRegistrationScreen) {
                CustomTopBar(
                    onSettingsClick = { /* Handle settings click */ },
                    onSearchClick = { /* Handle search click */ },
                    onProfileClick = { /* Handle profile click */ },
                            onToggleTheme = {}
                )
            }
        },
        bottomBar = {
            if (!isWelcomeScreen&&!isLoginScreen&&!isRegistrationScreen) {
                BottomNavigationBar(navController)
            }
        }
    ){ innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination, // Dynamic start destination
            modifier = Modifier.padding(innerPadding)
        ){

            composable(Screen.WelcomeScreen.route) {
                WelcomeScreen(
                    navigateToLoginScreen = { navController.navigate(Screen.LoginScreen.route) }
                )
            }

            composable(Screen.LoginScreen.route) {
                LoginScreen(
                    navigateBack = { navController.popBackStack() },
                    navigateToHomeScreen = { navController.navigate(Screen.Home.route) },
                    navigateToRegistrationScreen = {navController.navigate(Screen.RegistrationScreen.route)},
                    navigateToContactList = { navController.navigate(Screen.ContactListScreen.route) }
                )
            }
            composable(Screen.RegistrationScreen.route){
                RegistrationScreen(
                    navigateBack = { navController.popBackStack() },
                    navigateToContactList = { navController.navigate(Screen.ContactListScreen.route) },
                )
            }
            composable(Screen.ContactListScreen.route){
                ContactListScreen(
                    navigateToHome = { navController.navigate(Screen.Home.route) },
                )
            }



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
            composable(Screen.LogOutScreen.route){
                LogOut(
                    navigateBack = { navController.popBackStack() },
                    navigateToWelcomeScreen = { navController.navigate(Screen.WelcomeScreen.route) }
                )
            }

            composable(Screen.MainPlace.route) {
                MainTabsScreen(
                    navController = navController
                )
            }
            composable(
                route = Screen.GeofenceScreen.route,
                arguments = listOf(navArgument("placeId") { type = NavType.LongType })
            ) { backStackEntry ->
                val placeId = backStackEntry.arguments?.getLong("placeId") ?: 0L
                GeofenceScreen(placeId = placeId,
                    navigateToGeofenceDetail = { id ->
                    navController.navigate("geofence_detail_screen/$id")
                })
            }
            // Navigation setup for placelistscreen from placescreen
            composable(
                route = Screen.PlaceListScreen.route,
                arguments = listOf(navArgument("userId") { type = NavType.LongType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
                PlaceListScreen(
                    navigateBack = { navController.popBackStack() }, // Correct back navigation
                    navigateToGeofenceScreen = { id ->
                        navController.navigate("geofence_screen/$id")
                    }
                )
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
