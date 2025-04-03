package com.jlss.placelive.ui.Components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GradientAppName() {
    Text(
        text = "PlaceLive",
        fontSize = 26.sp, // Bigger text size
        fontWeight = FontWeight.Bold, // Makes it stand out
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF000000),
                    Color(0xFF51A4FC)


                )
            )
        ),
        modifier = Modifier
    )
}

@Composable
fun JLSSName() {
    Text(
        text = "JLSS",
        fontSize = 50.sp, // Bigger text size
        fontWeight = FontWeight.Bold, // Makes it stand out
        style = TextStyle(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFF8BF2F),// Orange
                            Color(0xFFF8682F)
                )
            )
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
    )
}



@Composable
fun HeartIconButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Favorite, // Heart Icon
            contentDescription = "Heart Icon",
            tint = Color(0xFF60A0FF), // Light red-pink shade (near white)
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )
    }
}
