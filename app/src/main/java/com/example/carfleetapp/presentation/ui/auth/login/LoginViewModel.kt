package com.example.carfleetapp.presentation.ui.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TokenStorage
import com.example.domain.model.auth.UserLogin
import com.example.domain.usecase.auth.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val tokenStorage: TokenStorage
) : ViewModel() {

    var phone by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun login(onSuccess: () -> Unit) {
        if (phone.isBlank() || password.isBlank()) {
            errorMessage = "Заполните все поля"
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val result = loginUseCase(UserLogin(phone, password))

            result.onSuccess { authResult ->
                tokenStorage.saveTokens(
                    accessToken = authResult.accessToken,
                    refreshToken = authResult.refreshToken,
                    expiresIn = authResult.expiresInSeconds,
                    role = authResult.role
                )

                isLoading = false
                onSuccess()
            }

            result.onFailure { exception ->
                isLoading = false
                errorMessage = exception.message ?: "Ошибка авторизации"
            }
        }
    }
}