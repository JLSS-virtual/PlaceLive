package com.jlss.placelive.ui.Screens.welcomescreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    navigateToLoginScreen: () -> Unit // Function type remains the same
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally, // Aligns items in the center
        verticalArrangement = Arrangement.Center // Centers content vertically
    ) {
        Spacer(modifier = Modifier.height(16.dp)) // Space between animation & text
        Text(text = "Welcoming!!!", fontSize = 20.sp) // Text below animation

        Spacer(modifier = Modifier.height(16.dp)) // Space before button

        Button(onClick = { navigateToLoginScreen() }) { // Corrected function call
            Text("Let's explore the app, Dear -> ")
        }
    }
}
