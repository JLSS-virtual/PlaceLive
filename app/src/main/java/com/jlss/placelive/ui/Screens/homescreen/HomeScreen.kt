package com.example.placelive.ui.screens

import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jlss.placelive.R
import com.jlss.placelive.ui.Components.MainAnimation
import com.jlss.placelive.ui.Components.MainSearchBar

@Composable
fun HomeScreen() {

    // top component includes glob, name, search bar
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy((-10).dp), // Reduce extra space
                horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(top = 5.dp) // Adjust top padding
        ) {
            MainAnimation(modifier = Modifier.size(200.dp)) // Adjust animation size
            Text(
                text = "JLSS",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF000000),
                            Color(0xFF79FDF9)
                        )
                    )
                ),
                modifier = Modifier.offset(y = (-70).dp) // Adjust to sit above animation
            )
        }

        Spacer(modifier = Modifier.height(1.dp)) // Reduce spacing further

        MainSearchBar()

        Spacer(modifier = Modifier.height(10.dp)) // Reduce spacing further
        // main body column includes searched items
        Spacer(modifier = Modifier.height(16.dp)) // Space between animation & text
        Text(text = "Coming soon!!!", fontSize = 20.sp) // Text below animation


    }
}

