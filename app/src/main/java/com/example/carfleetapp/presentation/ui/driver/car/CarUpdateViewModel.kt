package com.example.carfleetapp.presentation.ui.driver.car

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.car.Car
import com.example.domain.usecase.car.GetCarUseCase
import com.example.domain.usecase.car.SendToMaintenanceUseCase
import com.example.domain.usecase.car.UpdateCarDataUseCase
import com.example.domain.usecase.tripslot.GetMySlotsUseCase
import kotlinx.coroutines.launch

class CarUpdateViewModel(
    private val getCarUseCase: GetCarUseCase,
    private val updateCarUseCase: UpdateCarDataUseCase,
    private val maintenanceUseCase: SendToMaintenanceUseCase,
    private val getSlotsUseCase: GetMySlotsUseCase
) : ViewModel() {

    var car by mutableStateOf<Car?>(null)
    var mileage by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var isSaving by mutableStateOf(false)

    var showMaintenanceDialog by mutableStateOf(false)
    var maintenanceComment by mutableStateOf("")

    fun loadData() {
        viewModelScope.launch {
            isLoading = true
            val slotsResult = getSlotsUseCase.invoke()
            val activeCarId = slotsResult.getOrNull()
                ?.firstOrNull { it.status == 0 || it.status == 1 }?.carId

            if (activeCarId != null) {
                getCarUseCase(activeCarId).onSuccess {
                    car = it
                    mileage = it.currentMileageKm.toString()
                }
            }
            isLoading = false
        }
    }

    fun saveChanges() {
        val currentCar = car ?: return
        viewModelScope.launch {
            isSaving = true
            updateCarUseCase(currentCar, mileage.toIntOrNull() ?: currentCar.currentMileageKm)
                .onSuccess { isSaving = false }
        }
    }

    fun sendToMaintenance() {
        val currentCar = car ?: return
        viewModelScope.launch {
            isSaving = true
            maintenanceUseCase(currentCar.id, maintenanceComment)
                .onSuccess {
                    showMaintenanceDialog = false
                    loadData()
                }
            isSaving = false
        }
    }
}
