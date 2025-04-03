package com.jlss.placelive.viewmodel.registrationviewmodel

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jlss.placelive.data.api.UserApi
import com.jlss.placelive.model.User
import com.jlss.placelive.utility.UserPreferences.saveUserIdToPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val apiService: UserApi) : ViewModel() {

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> get() = _registrationState


    fun registerUser(context:Context,user: User) {
        _registrationState.value = RegistrationState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.addUser(user)
                if (response.isSuccessful) {
                    val userId = response.body()?.data?.id // Assuming API returns user ID
                    if (userId != null) {
                        saveUserIdToPreferences(userId,context) // Store in SharedPreferences
                        _registrationState.value = RegistrationState.Success(user)
                    } else {
                        _registrationState.value = RegistrationState.Error("User ID missing in response")
                    }
                } else {
                    _registrationState.value = RegistrationState.Error("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                _registrationState.value = RegistrationState.Error("Network error: ${e.message}")
            }
        }
    }



    sealed class RegistrationState {
        object Idle : RegistrationState()
        object Loading : RegistrationState()
        data class Success(val user: User?) : RegistrationState()
        data class Error(val message: String?) : RegistrationState()
    }

    companion object {
        fun Factory(apiService: UserApi): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return UserViewModel(apiService) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}