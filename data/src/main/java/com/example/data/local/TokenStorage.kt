package com.example.data.local

import android.content.Context
import android.content.SharedPreferences

class TokenStorage(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val EXPIRES_IN_KEY = "expires_in_seconds"
        private const val USER_ROLE_KEY = "user_role"
    }

    fun saveTokens(accessToken: String, refreshToken: String, expiresIn: Int, role: Int) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN_KEY, accessToken)
        editor.putString(REFRESH_TOKEN_KEY, refreshToken)
        editor.putInt(EXPIRES_IN_KEY, expiresIn)
        editor.putInt(USER_ROLE_KEY, role)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN_KEY, null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString(REFRESH_TOKEN_KEY, null)
    }

    fun getUserRole(): Int {
        return prefs.getInt(USER_ROLE_KEY, -1)
    }

    suspend fun clearTokens() {
        prefs.edit().clear().apply()
    }
}
