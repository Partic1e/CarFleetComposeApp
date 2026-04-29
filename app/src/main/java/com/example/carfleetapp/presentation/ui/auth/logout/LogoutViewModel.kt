package com.example.carfleetapp.presentation.ui.auth.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.auth.LogoutUseCase
import kotlinx.coroutines.launch

class LogoutViewModel(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onSuccess()
        }
    }
}