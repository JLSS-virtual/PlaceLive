package com.jlss.placelive.ui.Components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MainSearchBar() {
    var searchText by remember { mutableStateOf("") }

    // List of dynamic placeholder texts
    val placeholders = listOf(
        "Search place...",
        "Search friend...",
        "Search item...",
        "Search friends near an item..."
    )

    var currentPlaceholder by remember { mutableStateOf(placeholders[0]) }

    // LaunchedEffect to update placeholder every 3 seconds
    LaunchedEffect(Unit) {
        var index = 0
        while (true) {
            delay(3000) // Change placeholder every 3 seconds
            index = (index + 1) % placeholders.size
            currentPlaceholder = placeholders[index]
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(horizontal = 16.dp)
            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.small)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            textStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
            modifier = Modifier.weight(1f),
            decorationBox = { innerTextField ->
                if (searchText.isEmpty()) {
                    TextPlaceholder(text = currentPlaceholder) // Show dynamic placeholder
                }
                innerTextField()
            }
        )

        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Search Icon",
            tint = Color.Gray
        )
    }
}

@Composable
fun TextPlaceholder(text: String) {
    Box {
        androidx.compose.material3.Text(
            text = text,
            style = TextStyle(fontSize = 20.sp, color = Color.Gray)
        )
    }
}
