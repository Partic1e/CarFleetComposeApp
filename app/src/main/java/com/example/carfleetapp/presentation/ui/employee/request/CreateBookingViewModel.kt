package com.example.carfleetapp.presentation.ui.employee.request

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.booking.TripRequest
import com.example.domain.usecase.booking.RequestTripUseCase
import kotlinx.coroutines.launch

class CreateBookingViewModel(
    private val requestTripUseCase: RequestTripUseCase
) : ViewModel() {

    var fromAddress by mutableStateOf("")
    var toAddress by mutableStateOf("")
    var date by mutableStateOf("")
    var time by mutableStateOf("")
    var seats by mutableStateOf("1")
    var purpose by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun clearFields() {
        fromAddress = ""
        toAddress = ""
        date = ""
        time = ""
        seats = "1"
        purpose = ""
        errorMessage = null
    }

    fun submitRequest(onSuccess: (bookingId: String) -> Unit) {
        if (fromAddress.isBlank() || toAddress.isBlank() || date.isBlank() || time.isBlank()) {
            errorMessage = "Заполните обязательные поля"
            return
        }

        isLoading = true
        errorMessage = null

        val desiredDeparture = "${date}T${time}:00Z"

        viewModelScope.launch {
            val request = TripRequest(
                fromAddress = fromAddress,
                toAddress = toAddress,
                desiredDeparture = desiredDeparture,
                seats = seats.toIntOrNull() ?: 1,
                purpose = purpose
            )

            val result = requestTripUseCase(request)

            result.onSuccess { response ->
                isLoading = false
                clearFields()
                onSuccess(response.bookingId)
            }
            result.onFailure { exception ->
                isLoading = false
                errorMessage = exception.message ?: "Ошибка сервера"
            }
        }
    }
}
