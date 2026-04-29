package com.example.carfleetapp.presentation.ui.auth.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterStepOne(
    onNextStep: () -> Unit,
    onBack: () -> Unit,
    viewModel: RegisterViewModel
) {
    val roles = listOf("Водитель" to 1, "Сотрудник" to 2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 20.dp)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Создание аккаунта", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Шаг 1 из 2", color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(bottom = 32.dp))

        OutlinedTextField(
            value = viewModel.phone,
            onValueChange = { viewModel.phone = it },
            label = { Text("Номер телефона") },
            placeholder = { Text("+79990000000") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Пароль (мин. 8 символов)") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            shape = RoundedCornerShape(12.dp)
        )

        Text("Ваша роль в системе:", color = Color.White, modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
            roles.forEachIndexed { index, (label, value) ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = roles.size),
                    onClick = { viewModel.role = value },
                    selected = viewModel.role == value,
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary,
                        activeContentColor = Color.White,
                        inactiveContainerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(label)
                }
            }
        }

        Button(
            onClick = {
                if(viewModel.phone.length >= 11 && viewModel.password.length >= 8) onNextStep()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Далее", fontWeight = FontWeight.Bold)
        }

        TextButton(onClick = onBack) {
            Text("Уже есть аккаунт? Войти", color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun RegisterStepTwo(
    onRegisterSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: RegisterViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 20.dp)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Личные данные", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
        Text("Шаг 2 из 2", color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(bottom = 32.dp))

        OutlinedTextField(
            value = viewModel.lastName, onValueChange = { viewModel.lastName = it },
            label = { Text("Фамилия *") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = viewModel.firstName, onValueChange = { viewModel.firstName = it },
            label = { Text("Имя *") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = viewModel.patronymic ?: "", onValueChange = { viewModel.patronymic = it.ifBlank { null } },
            label = { Text("Отчество (опционально)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = viewModel.post, onValueChange = { viewModel.post = it },
            label = { Text("Должность *") },
            placeholder = { Text("Например: Старший водитель") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = viewModel.email ?: "", onValueChange = { viewModel.email = it.ifBlank { null } },
            label = { Text("Email (опционально)") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp)
        )

        if (viewModel.errorMessage != null) {
            Text(
                text = viewModel.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = { viewModel.register(onSuccess = onRegisterSuccess) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !viewModel.isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
            } else {
                Text("Завершить регистрацию", fontWeight = FontWeight.Bold)
            }
        }

        TextButton(onClick = onBack) {
            Text("Вернуться назад", color = MaterialTheme.colorScheme.secondary)
        }
    }
}
