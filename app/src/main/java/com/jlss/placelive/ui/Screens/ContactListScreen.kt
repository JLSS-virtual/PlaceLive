package com.jlss.placelive.ui.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jlss.placelive.data.api.RetrofitClient
import com.jlss.placelive.model.User
import com.jlss.placelive.repository.ContactsRepository
import com.jlss.placelive.viewmodel.ContactsViewModel

@Composable
fun ContactListScreen(navigateToHome:()->Unit) {
    val context = LocalContext.current
    val repository = remember { ContactsRepository(context.applicationContext) }
    val apiService = remember { RetrofitClient().createUserApi() }

    // Match GeofenceScreen initialization pattern
    val viewModel: ContactsViewModel = viewModel(
        factory = ContactsViewModel.Factory(
            apiService = apiService,
            repository = repository
        )
    )

    // Trigger contact fetch when screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchContacts(context)
    }

    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (!error.isNullOrEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(12.dp)
            ) {
                Text(
                    text = "Skip for now ",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Button(onClick = { navigateToHome() }) {
                Text("Go to Home")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(users.size) { index ->
                    UserListItem(user = users[index])
                }
            }
        }
    }
}

@Composable
fun UserListItem(
    user: User
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = user.name ?: "Unknown",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Phone: ${user.mobileNumber ?: "N/A"}",
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Phone: ${user.email ?: "N/A"}",
                fontSize = 14.sp
            )
        }
    }

}