package com.example.carfleetapp.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogoutSuccess: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${state.lastName.take(1)}${state.firstName.take(1)}".uppercase(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${state.lastName} ${state.firstName}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        val roleName = if (state.user?.role == 1) "Водитель" else "Сотрудник"
        Surface(
            color = MaterialTheme.colorScheme.secondaryContainer,
            shape = CircleShape,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            Text(
                text = roleName,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                ProfileInfoRow(icon = Icons.Default.Phone, label = "Телефон", value = state.user?.phoneNumber ?: "—")

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                )

                if (state.isEditing) {
                    EditableField("Фамилия", state.lastName, viewModel::onLastNameChange)
                    EditableField("Имя", state.firstName, viewModel::onFirstNameChange)
                    EditableField("Отчество", state.patronymic, viewModel::onPatronymicChange)
                    EditableField("Email", state.email, viewModel::onEmailChange)
                    EditableField("Должность", state.post, viewModel::onPostChange)
                } else {
                    ProfileInfoRow(icon = Icons.Default.Email, label = "Email", value = state.email)
                    ProfileInfoRow(icon = Icons.Default.Work, label = "Должность", value = state.post)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (state.isEditing) {
            Button(
                onClick = { viewModel.saveProfile() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                else Text("Сохранить изменения")
            }
            TextButton(onClick = { viewModel.toggleEdit() }) {
                Text("Отмена")
            }
        } else {
            Button(
                onClick = { viewModel.toggleEdit() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Редактировать профиль")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onLogoutSuccess,
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Default.ExitToApp, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Выйти из системы")
        }
    }
}

@Composable
fun ProfileInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value.ifBlank { "Не указано" }, fontSize = 16.sp)
        }
    }
}

@Composable
fun EditableField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}
