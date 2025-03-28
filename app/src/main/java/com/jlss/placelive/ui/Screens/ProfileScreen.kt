package com.jlss.placelive.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlss.placelive.ui.Components.FriendLoader

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center, // Centers content in the screen
        horizontalAlignment = Alignment.CenterHorizontally // Aligns items in the center
    ) {
        FriendLoader(modifier = Modifier.size(250.dp)) // Animation
        Spacer(modifier = Modifier.height(16.dp)) // Space between animation & text
        Text(text = "Coming soon!!!", fontSize = 20.sp) // Text below animation
    }
}
