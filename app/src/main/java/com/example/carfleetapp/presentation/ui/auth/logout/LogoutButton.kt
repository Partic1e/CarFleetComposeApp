package com.example.carfleetapp.presentation.ui.auth.logout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel

@Composable
fun LogoutIconButton(
    onLoggedOut: () -> Unit,
    viewModel: LogoutViewModel = koinViewModel()
) {
    IconButton(onClick = { viewModel.logout(onSuccess = onLoggedOut) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Выйти из аккаунта",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
