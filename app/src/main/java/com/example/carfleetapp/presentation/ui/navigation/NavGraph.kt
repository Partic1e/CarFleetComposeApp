package com.example.carfleetapp.presentation.ui.navigation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.carfleetapp.presentation.ui.auth.login.LoginScreen
import com.example.carfleetapp.presentation.ui.auth.logout.LogoutViewModel
import com.example.carfleetapp.presentation.ui.auth.registration.RegisterStepOne
import com.example.carfleetapp.presentation.ui.auth.registration.RegisterStepTwo
import com.example.carfleetapp.presentation.ui.auth.registration.RegisterViewModel
import com.example.carfleetapp.presentation.ui.map.YandexMapPickerScreen
import com.example.carfleetapp.presentation.ui.driver.DriverMainScreen
import com.example.carfleetapp.presentation.ui.employee.EmployeeMainScreen
import com.example.carfleetapp.presentation.ui.map.DriverNavigationScreen
import com.example.data.local.TokenStorage
import com.example.domain.model.tripslot.Waypoint
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun RequestLocationPermissions() {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.values.all { it }
        if (granted) {
            // Разрешения получены
        } else {
            // Пользователь отказал
        }
    }

    LaunchedEffect(Unit) {
        val fineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fineLocation != PackageManager.PERMISSION_GRANTED || coarseLocation != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FleetNavGraph(
    tokenStorage: TokenStorage = koinInject()
) {
    val navController = rememberNavController()
    val regViewModel: RegisterViewModel = koinViewModel()
    val mainViewModel: LogoutViewModel = koinViewModel()
    val gson = remember { com.google.gson.Gson() }

    val startDestination = remember {
        val hasValidToken = !tokenStorage.getAccessToken().isNullOrBlank()
        val role = tokenStorage.getUserRole()
        if (hasValidToken) {
            if (role == 1) "driver_home" else "employee_home"
        } else {
            "login"
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {

        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    val role = tokenStorage.getUserRole()
                    val targetRoute = if (role == 1) "driver_home" else "employee_home"
                    navController.navigate(targetRoute) {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("registration_flow") }
            )
        }

        navigation(startDestination = "step_one", route = "registration_flow") {
            composable("step_one") {
                RegisterStepOne(
                    onNextStep = { navController.navigate("step_two") },
                    onBack = { navController.popBackStack() },
                    viewModel = regViewModel
                )
            }
            composable("step_two") {
                RegisterStepTwo(
                    onRegisterSuccess = {
                        val role = tokenStorage.getUserRole()
                        val targetRoute = if (role == 1) "driver_home" else "employee_home"
                        navController.navigate(targetRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() },
                    viewModel = regViewModel
                )
            }
        }

        composable("driver_home") {
            DriverMainScreen(
                onLogoutSuccess = {
                    mainViewModel.logout {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                },
                onOpenNavigator = { waypoints ->
                    val waypointsJson = gson.toJson(waypoints)
                    val encodedJson = java.net.URLEncoder.encode(waypointsJson, "UTF-8")

                    navController.navigate("driver_navigation/$encodedJson")
                }
            )
        }

        composable(
            route = "driver_navigation/{waypointsJson}",
            arguments = listOf(
                navArgument("waypointsJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val encodedJson = backStackEntry.arguments?.getString("waypointsJson") ?: ""
            val waypointsJson = java.net.URLDecoder.decode(encodedJson, "UTF-8")

            val itemType = object : com.google.gson.reflect.TypeToken<List<Waypoint>>() {}.type
            val waypoints: List<Waypoint> = gson.fromJson(waypointsJson, itemType)

            DriverNavigationScreen(
                waypoints = waypoints,
                onTripFinished = { navController.popBackStack() }
            )
        }

        composable("employee_home") { backStackEntry ->
            val createFrom by backStackEntry.savedStateHandle.getStateFlow<String?>("from_address", null).collectAsState()
            val createTo by backStackEntry.savedStateHandle.getStateFlow<String?>("to_address", null).collectAsState()
            val updateFrom by backStackEntry.savedStateHandle.getStateFlow<String?>("update_from_address", null).collectAsState()
            val updateTo by backStackEntry.savedStateHandle.getStateFlow<String?>("update_to_address", null).collectAsState()

            EmployeeMainScreen(
                onLogoutSuccess = {
                    mainViewModel.logout {
                        navController.navigate("login") { popUpTo(0) { inclusive = true } }
                    }
                },
                onOpenMapForCreateFrom = { navController.navigate("map_picker/from") },
                onOpenMapForCreateTo = { navController.navigate("map_picker/to") },
                createFromAddress = createFrom,
                createToAddress = createTo,

                onOpenMapForUpdateFrom = { navController.navigate("map_picker/update_from") },
                onOpenMapForUpdateTo = { navController.navigate("map_picker/update_to") },
                updatedFromAddress = updateFrom,
                updatedToAddress = updateTo,

                clearMapResult = {
                    backStackEntry.savedStateHandle.remove<String>("from_address")
                    backStackEntry.savedStateHandle.remove<String>("to_address")
                    backStackEntry.savedStateHandle.remove<String>("update_from_address")
                    backStackEntry.savedStateHandle.remove<String>("update_to_address")
                }
            )
        }

        composable(
            route = "map_picker/{addressType}",
            arguments = listOf(navArgument("addressType") { type = NavType.StringType })
        ) { backStackEntry ->
            val addressType = backStackEntry.arguments?.getString("addressType") ?: "from"

            YandexMapPickerScreen(
                onLocationSelected = { selectedAddress ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("${addressType}_address", selectedAddress)

                    navController.popBackStack()
                }
            )
        }
    }
}
