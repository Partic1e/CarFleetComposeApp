package com.example.carfleetapp.presentation.ui.auth.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.TokenStorage
import com.example.domain.model.auth.UserRegistration
import com.example.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val tokenStorage: TokenStorage
) : ViewModel() {
    var phone by mutableStateOf("")
    var password by mutableStateOf("")
    var role by mutableIntStateOf(1)

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var patronymic by mutableStateOf<String?>(null)
    var email by mutableStateOf<String?>(null)
    var post by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun register(onSuccess: () -> Unit) {
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val user = UserRegistration(
                phoneNumber = phone, password = password, role = role,
                firstName = firstName, lastName = lastName,
                patronymic = patronymic, email = email, post = post
            )

            val result = registerUseCase(user)

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
                errorMessage = exception.message ?: "Неизвестная ошибка"
            }
        }
    }
}
