//package com.jlss.placelive.navigation
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.placelive.ui.screens.HomeScreen
//import com.jlss.placelive.ui.Screens.registrationscreen.LoginScreen
//
//
//
//@Composable
//fun AppNavigation() {
//    val navController = rememberNavController()
//    NavHost(
//        navController = navController,
//        startDestination = "login_screen"
//    ) {
//        composable("login_screen") { LoginScreen(navController) }
//        composable("home_screen") { HomeScreen(navController) }
//    }
//}
