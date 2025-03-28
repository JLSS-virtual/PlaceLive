package com.jlss.placelive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import com.jlss.placelive.navigation.AppNavigation
import com.jlss.placelive.ui.theme.PlaceLiveTheme
import com.jlss.placelive.ui.theme.ThemePreferenceManager
import com.jlss.placelive.database.DatabaseInstance
import com.jlss.placelive.websocket.WebSocketManager

/**
 * ## MainActivity - The entry point of the PlaceLive App
 * ---
 * **Essence of this class:**
 * - It sets up the user interface and manages the app’s **theme preferences**.
 * - It initializes **WebSocketManager** to handle **real-time updates**.
 * - It ensures that geofencing data is **synchronized** in the background.
 * - Uses **Jetpack Compose** for building UI.
 * - Uses **Kotlin Coroutines & Flow** for asynchronous operations.
 */
class MainActivity : ComponentActivity() {

    // Object for managing theme preferences (Light Mode / Dark Mode)
    private lateinit var themePreferenceManager: ThemePreferenceManager

    // WebSocketManager handles real-time location tracking and updates
    private lateinit var webSocketManager: WebSocketManager

    /**
     * ## onCreate - Lifecycle method called when activity is first created
     * ---
     * **Core Logic:**
     * - Initializes necessary managers (`themePreferenceManager`, `WebSocketManager`).
     * - Loads saved theme preferences asynchronously.
     * - Starts WebSocket connection for real-time updates.
     * - Sets up UI using **Jetpack Compose**.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 1: Initialize ThemePreferenceManager to manage dark/light mode
        themePreferenceManager = ThemePreferenceManager(this)

        // Step 2: Start background synchronization for geofence updates
        scheduleGeofenceSync(this)

        // Step 3: Initialize WebSocket for real-time data streaming
        webSocketManager = WebSocketManager.getInstance(this)
        webSocketManager.connect() // Establish WebSocket connection

        /**
         * ## UI Initialization using Jetpack Compose
         * ---
         * - Uses **remember** and **LaunchedEffect** to maintain and observe theme settings.
         * - **AppNavigation** is the core navigation component that handles screen transitions.
         */
        setContent {
            // Variable to hold the current theme state (dark/light mode)
            var darkMode by remember { mutableStateOf(false) }

            // Load saved theme preference asynchronously
            LaunchedEffect(Unit) {
                darkMode =  themePreferenceManager.isDarkMode.first() // Fetch theme from storage
            }

            // Observe theme changes and update UI accordingly
            LaunchedEffect(Unit) {
                themePreferenceManager.isDarkMode.collectLatest { newMode ->
                    darkMode = newMode
                }
            }

            // Apply theme and initialize the app's main UI
            PlaceLiveTheme(isDarkMode = darkMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(
                        toggleTheme = { newMode: Boolean ->
                            if (newMode != darkMode) {
                                darkMode = newMode
                                lifecycleScope.launch {
                                    themePreferenceManager.saveThemePreference(newMode)
                                }
                            }
                        },
                        isDarkMode = darkMode
                    )
                }
            }
        }
    }

    /**
     * ## onDestroy - Lifecycle method called before activity is destroyed
     * ---
     * **Core Logic:**
     * - Ensures WebSocket connection is properly closed to prevent memory leaks.
     * - Called automatically when the user exits the app or activity is killed.
     */
    override fun onDestroy() {
        super.onDestroy()
        webSocketManager.disconnect() // Safely close WebSocket connection
    }
}
