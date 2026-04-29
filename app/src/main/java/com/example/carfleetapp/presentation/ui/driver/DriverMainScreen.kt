package com.example.carfleetapp.presentation.ui.driver

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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.carfleetapp.presentation.ui.driver.car.CarUpdateScreen
import com.example.carfleetapp.presentation.ui.driver.list.DriverTripSlotsScreen
import com.example.carfleetapp.presentation.ui.navigation.BottomNavItem
import com.example.carfleetapp.presentation.ui.navigation.CustomSlidingNavBar
import com.example.carfleetapp.presentation.ui.navigation.RequestLocationPermissions
import com.example.carfleetapp.presentation.ui.profile.ProfileScreen
import com.example.data.local.NotificationManager
import com.example.domain.model.tripslot.Waypoint
import org.koin.compose.koinInject

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DriverMainScreen(
    onLogoutSuccess: () -> Unit,
    onOpenNavigator: (List<Waypoint>) -> Unit
) {
    val notificationManager: NotificationManager = koinInject()

    LaunchedEffect(Unit) {
        notificationManager.syncToken()
    }

    RequestLocationPermissions()

    val items = listOf(
        BottomNavItem.CarStatus,
        BottomNavItem.DriverTrips,
        BottomNavItem.Profile
    )

    var selectedItem by remember { mutableIntStateOf(1) }

    Scaffold(
        bottomBar = {
            CustomSlidingNavBar(
                items = items,
                selectedIndex = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            AnimatedContent(
                targetState = selectedItem,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
                            scaleIn(initialScale = 0.95f, animationSpec = tween(220, delayMillis = 90)))
                        .togetherWith(fadeOut(animationSpec = tween(90)))
                },
                label = "screenTransition"
            ) { targetIndex ->
                when (targetIndex) {
                    0 -> CarUpdateScreen()
                    1 -> DriverTripSlotsScreen(onOpenNavigator = onOpenNavigator)
                    2 -> ProfileScreen(onLogoutSuccess = onLogoutSuccess)
                }
            }
        }
    }
}
