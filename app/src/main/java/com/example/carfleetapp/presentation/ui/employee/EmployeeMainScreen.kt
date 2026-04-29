package com.example.carfleetapp.presentation.ui.employee

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.carfleetapp.presentation.ui.employee.list.EmployeeBookingsScreen
import com.example.carfleetapp.presentation.ui.employee.request.CreateBookingScreen
import com.example.carfleetapp.presentation.ui.navigation.BottomNavItem
import com.example.carfleetapp.presentation.ui.navigation.CustomSlidingNavBar
import com.example.carfleetapp.presentation.ui.navigation.RequestLocationPermissions
import com.example.carfleetapp.presentation.ui.profile.ProfileScreen
import com.example.data.local.NotificationManager
import org.koin.compose.koinInject

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EmployeeMainScreen(
    onLogoutSuccess: () -> Unit,
    onOpenMapForCreateFrom: () -> Unit,
    onOpenMapForCreateTo: () -> Unit,
    createFromAddress: String?,
    createToAddress: String?,
    onOpenMapForUpdateFrom: () -> Unit,
    onOpenMapForUpdateTo: () -> Unit,
    updatedFromAddress: String?,
    updatedToAddress: String?,
    clearMapResult: () -> Unit
) {
    val notificationManager: NotificationManager = koinInject()

    LaunchedEffect(Unit) {
        notificationManager.syncToken()
    }

    RequestLocationPermissions()

    val items = listOf(
        BottomNavItem.CreateBooking,
        BottomNavItem.MyBookings,
        BottomNavItem.Profile
    )

    var selectedItem by rememberSaveable { mutableIntStateOf(1) }

    LaunchedEffect(createFromAddress, createToAddress) {
        if (createFromAddress != null || createToAddress != null) {
            selectedItem = 0
        }
    }

    Scaffold(
        bottomBar = {
            CustomSlidingNavBar(
                items = items,
                selectedIndex = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AnimatedContent(
                targetState = selectedItem,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            scaleIn(
                                initialScale = 0.92f,
                                animationSpec = tween(220, delayMillis = 90)
                            ))
                        .togetherWith(fadeOut(animationSpec = tween(90)))
                },
                label = "employee_screens_transition"
            ) { targetIndex ->
                when (targetIndex) {
                    0 -> CreateBookingScreen(
                        initialFromAddress = createFromAddress,
                        initialToAddress = createToAddress,
                        onOpenMapForFrom = onOpenMapForCreateFrom,
                        onOpenMapForTo = onOpenMapForCreateTo,
                        onBookingSuccess = {
                            selectedItem = 1
                        },
                        onBack = { selectedItem = 1 }
                    )

                    1 -> EmployeeBookingsScreen(
                        onOpenMapForUpdateFrom = onOpenMapForUpdateFrom,
                        onOpenMapForUpdateTo = onOpenMapForUpdateTo,
                        updatedFromAddress = updatedFromAddress,
                        updatedToAddress = updatedToAddress,
                        clearMapResult = clearMapResult
                    )

                    2 -> ProfileScreen(onLogoutSuccess = onLogoutSuccess)
                }
            }
        }
    }
}
