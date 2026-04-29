package com.example.carfleetapp.presentation.ui.employee.request

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.carfleetapp.presentation.ui.components.address.FleetAddressField
import com.example.carfleetapp.presentation.ui.components.date.FleetDatePickerDialog
import com.example.carfleetapp.presentation.ui.components.date.FleetDateTimePickerRow
import com.example.carfleetapp.presentation.ui.components.date.FleetTimePickerDialog
import com.example.carfleetapp.presentation.ui.components.textfield.FleetTextField
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CreateBookingScreen(
    initialFromAddress: String?,
    initialToAddress: String?,
    onOpenMapForFrom: () -> Unit,
    onOpenMapForTo: () -> Unit,
    onBookingSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: CreateBookingViewModel = koinViewModel()
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(initialFromAddress) {
        if (!initialFromAddress.isNullOrBlank()) {
            viewModel.fromAddress = initialFromAddress
        }
    }
    LaunchedEffect(initialToAddress) {
        if (!initialToAddress.isNullOrBlank()) {
            viewModel.toAddress = initialToAddress
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 20.dp, horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Новая поездка",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Ручной ввод или выбор на карте",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        FleetAddressField(
            value = viewModel.fromAddress,
            onValueChange = { viewModel.fromAddress = it },
            label = "Откуда (Адрес) *",
            onMapClick = onOpenMapForFrom
        )

        FleetAddressField(
            value = viewModel.toAddress,
            onValueChange = { viewModel.toAddress = it },
            label = "Куда (Адрес) *",
            onMapClick = onOpenMapForTo
        )

        FleetDateTimePickerRow(
            selectedDateText = viewModel.date,
            selectedTimeText = viewModel.time,
            onDateClick = { showDatePicker = true },
            onTimeClick = { showTimePicker = true }
        )

        FleetTextField(
            value = viewModel.seats,
            onValueChange = { viewModel.seats = it },
            label = "Количество мест",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        FleetTextField(
            value = viewModel.purpose,
            onValueChange = { viewModel.purpose = it },
            label = "Цель поездки (Опционально)",
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (viewModel.errorMessage != null) {
            Text(
                text = viewModel.errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                viewModel.submitRequest(onSuccess = { onBookingSuccess() })
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !viewModel.isLoading,
            shape = RoundedCornerShape(12.dp)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Отправить заявку", fontWeight = FontWeight.Bold)
            }
        }

        TextButton(onClick = onBack) {
            Text("Отмена", color = MaterialTheme.colorScheme.secondary)
        }
    }

    if (showDatePicker) {
        FleetDatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { millis ->
                if (millis != null) {
                    viewModel.date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
                }
            }
        )
    }

    if (showTimePicker) {
        FleetTimePickerDialog(
            onDismiss = { showTimePicker = false },
            onTimeSelected = { hour, minute ->
                val h = hour.toString().padStart(2, '0')
                val m = minute.toString().padStart(2, '0')
                viewModel.time = "$h:$m"
            }
        )
    }
}
