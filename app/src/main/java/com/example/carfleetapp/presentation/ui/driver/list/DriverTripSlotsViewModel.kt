package com.example.carfleetapp.presentation.ui.driver.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.tripslot.TripSlot
import com.example.domain.usecase.tripslot.GetMySlotsUseCase
import kotlinx.coroutines.launch

class DriverTripSlotsViewModel(
    private val getMySlotsUseCase: GetMySlotsUseCase
) : ViewModel() {

    var slots by mutableStateOf<List<TripSlot>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadSlots() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val result = getMySlotsUseCase()
            result.onSuccess {
                slots = it
                isLoading = false
            }
            result.onFailure {
                errorMessage = it.message ?: "Произошла ошибка"
                isLoading = false
            }
        }
    }
}
