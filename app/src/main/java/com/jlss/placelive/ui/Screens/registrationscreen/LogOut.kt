package com.jlss.placelive.ui.Screens.registrationscreen


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlss.placelive.ui.Components.Loader
import com.jlss.placelive.utility.UserPreferences

@Composable
fun LogOut(
    navigateBack: () -> Unit,
    navigateToWelcomeScreen: () -> Unit
) {
    val userId: Long = 123
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Log Out screen!!!", fontSize = 20.sp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(12.dp)
        ) {
            Text(
                text = "log out to go welcoe screen",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        Button(onClick = {
            UserPreferences.setUserLoggedIn(context, false)
            navigateToWelcomeScreen()
        }) {
            Text("LogOut")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(12.dp)
        ) {
            Text(
                text = "Go back and read more about app.",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        Button(onClick = navigateBack) {
            Text("back")
        }

    }

}