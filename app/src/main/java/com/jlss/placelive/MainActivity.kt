package com.jlss.placelive

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.jlss.placelive.data.api.RetrofitClient
import com.jlss.placelive.navigation.AppNavigation
import com.jlss.placelive.repository.ContactsRepository
import com.jlss.placelive.ui.theme.PlaceLiveTheme
import com.jlss.placelive.ui.theme.ThemePreferenceManager
import com.jlss.placelive.utility.UserPreferences
import com.jlss.placelive.viewmodel.ContactsViewModel

class MainActivity : ComponentActivity() {

    private lateinit var themePreferenceManager: ThemePreferenceManager
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    // Instantiate the ContactsViewModel using the singleton factory.
    private val contactsViewModel: ContactsViewModel by viewModels {
        ContactsViewModel.Factory(
            RetrofitClient().createUserApi(),
            ContactsRepository(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        themePreferenceManager = ThemePreferenceManager(this)
        scheduleGeofenceSync(this) // Ensure this function is defined appropriately

        // Initialize the permission launcher for READ_CONTACTS.
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                fetchContacts()
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }
        }

        // Check and request contacts permission.
        checkContactsPermission()

        setContent {
            var darkMode by remember { mutableStateOf(false) }
            var isLoggedIn by remember { mutableStateOf(UserPreferences.isUserLoggedIn(this)) }

            PlaceLiveTheme(isDarkMode = darkMode) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(isLoggedIn = isLoggedIn)
                }
            }
        }
    }

    // Check the READ_CONTACTS permission using the new Activity Result API.
    private fun checkContactsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            fetchContacts()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    // Fetch contacts using the ViewModel.
    private fun fetchContacts() {
        contactsViewModel.fetchContacts(this)
    }
}
