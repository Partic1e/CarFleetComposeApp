package com.example.carfleetapp.presentation.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.user.ProfileState
import com.example.domain.usecase.user.GetProfileUseCase
import com.example.domain.usecase.user.UpdateProfileUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {

    var state by mutableStateOf(ProfileState())
        private set

    fun loadProfile() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = getProfileUseCase()
            result.fold(
                onSuccess = { user ->
                    state = state.copy(
                        user = user,
                        firstName = user.firstName,
                        lastName = user.lastName,
                        patronymic = user.patronymic ?: "",
                        email = user.email ?: "",
                        post = user.post,
                        isLoading = false
                    )
                },
                onFailure = { state = state.copy(error = it.message, isLoading = false) }
            )
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            state = state.copy(isSaving = true)
            val result = updateProfileUseCase(
                firstName = state.firstName,
                lastName = state.lastName,
                patronymic = state.patronymic,
                email = state.email,
                post = state.post
            )
            result.fold(
                onSuccess = {
                    state = state.copy(isSaving = false, isEditing = false)
                    loadProfile()
                },
                onFailure = { state = state.copy(error = it.message, isSaving = false) }
            )
        }
    }

    fun toggleEdit() { state = state.copy(isEditing = !state.isEditing) }
    fun onFirstNameChange(v: String) { state = state.copy(firstName = v) }
    fun onLastNameChange(v: String) { state = state.copy(lastName = v) }
    fun onPatronymicChange(v: String) { state = state.copy(patronymic = v) }
    fun onEmailChange(v: String) { state = state.copy(email = v) }
    fun onPostChange(v: String) { state = state.copy(post = v) }
}
