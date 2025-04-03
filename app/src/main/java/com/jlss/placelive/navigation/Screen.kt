package com.example.placelive.ui.navigation

/**
 * **Screen** - Represents the navigation destinations in the Place Live app.
 *
 * ## **Core Concept**
 * - Defines a **sealed class** for type-safe navigation within the app.
 * - Uses **object declarations** for navigation routes.
 * - Supports both **static and dynamic routes**.
 *
 * ## **Essence & Logic**
 * - Prevents **hardcoded strings** in navigation.
 * - Ensures **type safety** when passing navigation arguments.
 * - Simplifies navigation logic with **predefined routes**.
 *
 * ## **Technology Stack**
 * - **Jetpack Compose Navigation**: Enables smooth, declarative navigation.
 * - **Sealed Classes**: Restricts subclassing and ensures all screens are defined in one place.
 * - **Parameterized Routes**: Allows screens to receive dynamic data.
 *
 * ## **Goal**
 * - Provide a **structured and maintainable** navigation system.
 * - Ensure **scalability** by allowing easy addition of new screens.
 * - Avoid **string-based navigation errors** by using type-safe routes.
 */
sealed class Screen(val route: String, val title: String? = null) {

    object WelcomeScreen : Screen("welcome_screen", "Welcome")
    object RegistrationScreen : Screen("registration_screen","Register")
    object ContactListScreen : Screen("contact_list_screen","Suggestions")

    object LogOutScreen : Screen("log_out_screen", "LogOut")
    object LoginScreen : Screen("login_screen", "Login")
    // 🔹 **Bottom Navigation Screens** (Static)
    object Home : Screen("home", "Home")
    object Profile : Screen("profile", "Friend Circle") // ✅ Renamed for clarity
    object Messages : Screen("messages", "Messages")
    object Events : Screen("events", "Events")

    // 🔹 **Extended Navigation Screens** (Static)
    object MainPlace : Screen("main_place", "Place Live")

    // 🔹 **Dynamic Navigation Screens** (Parameterized Routes)

    /**
     * **GeofenceScreen** - Displays a geofence based on the provided `placeId`.
     *
     * **Route Format:** `geofence_screen/{placeId}`
     * **Usage Example:**
     * ```kotlin
     * navController.navigate(Screen.GeofenceScreen.createRoute(123))
     * ```
     */
    object GeofenceScreen : Screen("geofence_screen/{placeId}") {
        fun createRoute(placeId: Long) = "geofence_screen/$placeId"
    }

    /**
     * **GeofenceDetailScreen** - Displays details of a geofence using `geofenceId`.
     *
     * **Route Format:** `geofence_detail_screen/{geofenceId}`
     * **Usage Example:**
     * ```kotlin
     * navController.navigate(Screen.GeofenceDetailScreen.createRoute(456))
     * ```
     */
    object GeofenceDetailScreen : Screen("geofence_detail_screen/{geofenceId}") {
        fun createRoute(geofenceId: Long) = "geofence_detail_screen/$geofenceId"
    }


    object PlaceListScreen : Screen("place_list_screen/{userId}") {
        fun createRoute(userId: Long) = "place_list_screen/$userId"
    }



}
