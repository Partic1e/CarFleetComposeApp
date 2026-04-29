package com.example.data.local

import com.example.data.model.notification.UpdateTokenRequest
import com.example.data.network.NotificationApi
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationManager(
    private val api: NotificationApi,
    private val tokenStorage: TokenStorage
) {
    fun syncToken() {
        val jwt = tokenStorage.getAccessToken()
        if (jwt == null) {
            return
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }

            val fcmToken = task.result

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.updateToken(UpdateTokenRequest(fcmToken))
                } catch (e: Exception) {

                }
            }
        }
    }
}