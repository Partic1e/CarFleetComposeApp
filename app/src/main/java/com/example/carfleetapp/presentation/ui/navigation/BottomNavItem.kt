package com.example.carfleetapp.presentation.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Route
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object CreateBooking : BottomNavItem("manual_booking", Icons.Default.AddBox, "Создать")
    object MyBookings : BottomNavItem("employee_home", Icons.AutoMirrored.Filled.ListAlt, "Заявки")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Профиль")
    object CarStatus : BottomNavItem("car_status", Icons.Default.DirectionsCar, "Автомобиль")
    object DriverTrips : BottomNavItem("driver_home", Icons.Default.Route, "Рейсы")
}
