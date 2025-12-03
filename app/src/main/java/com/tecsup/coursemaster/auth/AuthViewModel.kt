package com.tecsup.coursemaster.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoggedIn: Boolean = false
)

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Completa todos los campos")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.login(email, password)
            _uiState.value = if (result.isSuccess) {
                AuthUiState(isLoggedIn = true)
            } else {
                AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun register(email: String, password: String, confirm: String) {
        if (email.isBlank() || password.isBlank() || confirm.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Completa todos los campos")
            return
        }
        if (password != confirm) {
            _uiState.value = _uiState.value.copy(error = "Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = repository.register(email, password)
            _uiState.value = if (result.isSuccess) {
                AuthUiState(isLoggedIn = true)
            } else {
                AuthUiState(error = result.exceptionOrNull()?.message)
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.value = AuthUiState(isLoggedIn = false)
    }

    fun getCurrentUserId(): String? = repository.getCurrentUserId()
}
