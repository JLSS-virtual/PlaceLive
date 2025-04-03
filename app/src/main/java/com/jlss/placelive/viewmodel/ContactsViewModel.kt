package com.jlss.placelive.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jlss.placelive.data.api.UserApi
import com.jlss.placelive.model.MobileNumberRequest
import com.jlss.placelive.model.User
import com.jlss.placelive.repository.ContactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val repository: ContactsRepository,
    private val apiService: UserApi
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val users: StateFlow<List<User>> get() = _users
    val isLoading: StateFlow<Boolean> get() = _isLoading
    val error: StateFlow<String?> get() = _error

    fun fetchContacts(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val contacts = repository.getContacts(context)
                sendContactsToServer(contacts)
            } catch (e: Exception) {
                _error.value = "Failed to fetch contacts: ${e.localizedMessage}"
                Log.e("ContactsViewModel", "Error: ${e.stackTraceToString()}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun sendContactsToServer(contacts: List<String>) {
        try {
            val response = apiService.getUsersByMobileNumbers(contacts)
            if (response.isSuccessful) {
                response.body()?.data?.let { users ->
                    _users.value = users // Direct assignment, since 'users' is already a List<User>
                    Log.d("ContactsViewModel", "Found ${_users.value.size} users")
                } ?: run {
                    _error.value = "No users found in response"
                }
            } else {
                _error.value = "Server error: ${response.code()} - ${response.message()}"
            }
        } catch (e: Exception) {
            _error.value = "Network error: ${e.localizedMessage}"
            Log.e("ContactsViewModel", "API Error: ${e.stackTraceToString()}")
        }
    }


    fun clearError() {
        _error.value = null
    }

    companion object {
        @Volatile
        private var instance: ViewModelProvider.Factory? = null

        fun Factory(
            apiService: UserApi,
            repository: ContactsRepository
        ): ViewModelProvider.Factory {
            return instance ?: synchronized(this) {
                instance ?: object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
                            @Suppress("UNCHECKED_CAST")
                            return ContactsViewModel(repository, apiService) as T
                        }
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                    }
                }.also { instance = it }
            }
        }
    }
}
