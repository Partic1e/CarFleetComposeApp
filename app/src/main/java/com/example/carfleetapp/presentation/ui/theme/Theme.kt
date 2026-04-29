package com.example.carfleetapp.presentation.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = BrandPrimaryVariant,

    secondary = BrandSecondary,
    onSecondary = Color.White,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,

    background = LightBg,
    onBackground = LightOnSurf,

    surface = LightSurf,
    onSurface = LightOnSurf,
    surfaceVariant = LightContainer,
    onSurfaceVariant = LightTextSecondary,

    error = StatusError,
    onError = Color.White,
    outline = Color(0xFFE2E8F0)
)

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0C4A6E),
    onPrimaryContainer = Color(0xFFE0F2FE),

    secondary = BrandSecondary,
    onSecondary = Color.White,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,

    background = DarkBg,
    onBackground = DarkOnSurf,

    surface = DarkSurf,
    onSurface = DarkOnSurf,
    surfaceVariant = DarkContainer,
    onSurfaceVariant = DarkTextSecondary,

    error = StatusError,
    onError = Color.White,
    outline = Color(0xFF334155)
)

@Composable
fun FleetManagementTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
