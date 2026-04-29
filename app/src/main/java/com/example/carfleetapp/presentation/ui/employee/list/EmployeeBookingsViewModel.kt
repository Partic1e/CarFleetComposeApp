package com.example.carfleetapp.presentation.ui.employee.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.booking.Booking
import com.example.domain.model.booking.UpdateBooking
import com.example.domain.usecase.booking.CancelBookingUseCase
import com.example.domain.usecase.booking.GetMyBookingsUseCase
import com.example.domain.usecase.booking.UpdateBookingUseCase
import kotlinx.coroutines.launch

class EmployeeBookingsViewModel(
    private val getMyBookingsUseCase: GetMyBookingsUseCase,
    private val cancelBookingUseCase: CancelBookingUseCase,
    private val updateBookingUseCase: UpdateBookingUseCase
) : ViewModel() {

    var bookings by mutableStateOf<List<Booking>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadBookings() {
        isLoading = true
        errorMessage = null
        viewModelScope.launch {
            val result = getMyBookingsUseCase()
            result.onSuccess {
                bookings = it
                isLoading = false
            }
            result.onFailure {
                errorMessage = it.message ?: "Произошла ошибка"
                isLoading = false
            }
        }
    }

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            cancelBookingUseCase(bookingId)
                .onSuccess { success ->
                    if (success) {
                        loadBookings()
                    } else {
                        errorMessage = "Отмена не удалась: до выезда слишком мало времени"
                    }
                }
                .onFailure { error ->
                    errorMessage = "Ошибка при отмене: ${error.message}"
                    loadBookings()
                }
        }
    }

    fun updateBooking(bookingId: String, params: UpdateBooking) {
        viewModelScope.launch {
            isLoading = true
            updateBookingUseCase(bookingId, params)
                .onSuccess {
                    loadBookings()
                }
                .onFailure { error ->
                    errorMessage = "Ошибка при обновлении: ${error.message}"
                    isLoading = false
                }
        }
    }
}
