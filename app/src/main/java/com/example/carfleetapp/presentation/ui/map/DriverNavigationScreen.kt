package com.example.carfleetapp.presentation.ui.map

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.domain.model.tripslot.Waypoint
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.*
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.runtime.Error
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import com.example.carfleetapp.presentation.service.DriverTrackingService

fun createNavArrowBitmap(context: Context): Bitmap {
    val size = (32 * context.resources.displayMetrics.density).toInt()
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        color = "#0EA5E9".toColorInt()
        isAntiAlias = true
        style = Paint.Style.FILL
    }
    val path = Path().apply {
        moveTo(size / 2f, 0f)
        lineTo(size.toFloat(), size.toFloat())
        lineTo(size / 2f, size * 0.8f)
        lineTo(0f, size.toFloat())
        close()
    }
    canvas.drawPath(path, paint)
    return bitmap
}

@Composable
fun DriverNavigationScreen(
    waypoints: List<Waypoint>,
    onTripFinished: () -> Unit
) {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        val intent = Intent(context, DriverTrackingService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }

        onDispose {
            context.stopService(intent)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val mapView = remember { MapView(context) }
    val mapObjects: MapObjectCollection = mapView.map.mapObjects

    var currentStopIndex by remember { mutableIntStateOf(0) }
    val currentWaypoint = waypoints.getOrNull(currentStopIndex)
    val isLastStop = currentStopIndex == waypoints.size - 1

    val drivingRouter: DrivingRouter = remember {
        DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)
    }
    var drivingSession: DrivingSession? by remember { mutableStateOf(null) }

    val userLocationLayer: UserLocationLayer = remember {
        MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
        }
    }

    fun requestRouteTo(targetPoint: Point) {
        val userLocation = userLocationLayer.cameraPosition()?.target
            ?: Point(waypoints.first().lat!!, waypoints.first().lon!!)

        val requestPoints = listOf(
            RequestPoint(userLocation, RequestPointType.WAYPOINT, null, null),
            RequestPoint(targetPoint, RequestPointType.WAYPOINT, null, null)
        )

        drivingSession?.cancel()
        drivingSession = drivingRouter.requestRoutes(
            requestPoints,
            DrivingOptions(),
            VehicleOptions(),
            object : DrivingSession.DrivingRouteListener {
                override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
                    if (routes.isNotEmpty()) {
                        val route = routes[0]
                        mapObjects.clear()
                        mapObjects.addPolyline(route.geometry).apply {
                            strokeWidth = 6f
                            setStrokeColor("#0EA5E9".toColorInt())
                        }
                        mapView.map.move(
                            CameraPosition(userLocation, 17f, 0f, 45f),
                            Animation(Animation.Type.SMOOTH, 1.5f),
                            null
                        )
                    }
                }
                override fun onDrivingRoutesError(error: Error) {}
            }
        )
    }

    LaunchedEffect(currentStopIndex) {
        currentWaypoint?.let {
            if (it.lat != null && it.lon != null) {
                requestRouteTo(Point(it.lat!!, it.lon!!))
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                }
                Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            drivingSession?.cancel()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val badgeColor = if (currentWaypoint?.type == "Pickup")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.tertiary

                    Surface(
                        color = badgeColor,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = if (currentWaypoint?.type == "Pickup") "ПОСАДКА" else "ВЫСАДКА",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Остановка ${currentStopIndex + 1} из ${waypoints.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = currentWaypoint?.address ?: "Адрес не указан",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (isLastStop) {
                            onTripFinished()
                        } else {
                            currentStopIndex++
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLastStop) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isLastStop) "ЗАВЕРШИТЬ РЕЙС" else "ПРИБЫЛ / СЛЕДУЮЩАЯ ТОЧКА",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
