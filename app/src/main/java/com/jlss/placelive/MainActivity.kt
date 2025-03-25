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
import kotlinx.coroutines.runBlocking
import com.jlss.placelive.navigation.AppNavigation
import com.jlss.placelive.ui.theme.PlaceLiveTheme
import com.jlss.placelive.ui.theme.ThemePreferenceManager

class MainActivity : ComponentActivity() {
    private lateinit var themePreferenceManager: ThemePreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themePreferenceManager = ThemePreferenceManager(this)

        // Load the saved theme setting from DataStore
        val isDarkMode = runBlocking { themePreferenceManager.isDarkMode.first() }

        setContent {
            var darkMode by remember { mutableStateOf(isDarkMode) }

            // Collect theme changes from DataStore
            DisposableEffect(Unit) {
                val job = lifecycleScope.launch {
                    themePreferenceManager.isDarkMode.collectLatest { newMode: Boolean ->
                        darkMode = newMode
                    }
                }
                onDispose { job.cancel() }
            }

            PlaceLiveTheme(isDarkMode = darkMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(
                        toggleTheme = { newMode: Boolean ->  // ✅ Ensure correct parameter name
                            lifecycleScope.launch {
                                themePreferenceManager.saveThemePreference(newMode)
                            }
                        },
                        isDarkMode = darkMode
                    )
                }
            }
        }
    }
}
