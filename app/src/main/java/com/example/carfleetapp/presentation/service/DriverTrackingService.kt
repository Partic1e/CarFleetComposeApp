package com.example.carfleetapp.presentation.service

import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.data.model.location.LocationUpdateRequest
import com.example.data.network.TrackingApi
import com.google.android.gms.location.*
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class DriverTrackingService : Service() {

    private val trackingApi: TrackingApi by inject()
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                sendLocationToBackend(location)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        startForegroundService()
    }

    private fun startForegroundService() {
        val channelId = "tracking_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Tracking Location", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Выполняется рейс")
            .setContentText("Ваше местоположение передается в систему")
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    private fun startLocationUpdates() {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateDistanceMeters(10f)
            .build()

        try {
            fusedLocationClient.requestLocationUpdates(request, locationCallback, mainLooper)
        } catch (unlikely: SecurityException) {

        }
    }

    private fun sendLocationToBackend(location: Location) {
        serviceScope.launch {
            try {
                trackingApi.updateLocation(
                    LocationUpdateRequest(
                        location.latitude,
                        location.longitude
                    )
                )
            } catch (e: Exception) {

            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startLocationUpdates()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
