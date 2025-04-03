package com.jlss.placelive.ui.Screens.registrationscreen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jlss.placelive.data.api.RetrofitClient
import com.jlss.placelive.model.User
import com.jlss.placelive.model.UserRegion
import com.jlss.placelive.utility.UserPreferences
import com.jlss.placelive.viewmodel.registrationviewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    navigateBack: () -> Unit,
    navigateToContactList: () -> Unit
) {
    val retrofitClient = remember { RetrofitClient() }
    val viewModel: UserViewModel = viewModel(
        factory = UserViewModel.Factory(retrofitClient.createUserApi())
    )

    val context = LocalContext.current
    var id by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }

    // Region fields
    var country by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    val savedUser: User

    val registrationState by viewModel.registrationState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = mobile,
            onValueChange = { mobile = it },
            label = { Text("Mobile Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        // Region Fields
        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Country") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state,
            onValueChange = { state = it },
            label = { Text("State") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = registrationState) {
            is UserViewModel.RegistrationState.Loading -> {
                CircularProgressIndicator()
            }
            is UserViewModel.RegistrationState.Error -> {
                Text(
                    text = state.message ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error
                )
            }
            is UserViewModel.RegistrationState.Success -> {
                LaunchedEffect(Unit) {
                    navigateToContactList()// navigating to contactlists to show suggested users taht uses the app and tehn hjoem screen or profilescreen that will present in friend circle
                }
            }
            else -> {}
        }

        Button(
            onClick = {
                val userRegion = UserRegion(
                    country = country,
                    state = state,
                    city = city,
                    street = ""
                )
                val newUser = User(
                    name = name,
                    email = email,
                    mobileNumber = mobile,
                    userRegion = userRegion,
                    // Add other required fields
                    userBio = "",
                    followers = emptyList(),
                    following = emptyList(),
                    closeFriends = emptyList(),
                    profileImageUrl = "",
                )
                viewModel.registerUser(context,newUser)
                // updateing isloggedin state
                UserPreferences.setUserLoggedIn(context, true)
                navigateToContactList()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register")
        }

        TextButton(onClick = navigateBack) {
            Text("Already have an account? Login")
        }
    }
}