package com.example.carfleetapp.presentation.ui.employee.update

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.carfleetapp.presentation.ui.components.address.FleetAddressField
import com.example.carfleetapp.presentation.ui.components.date.FleetDatePickerDialog
import com.example.carfleetapp.presentation.ui.components.date.FleetDateTimePickerRow
import com.example.carfleetapp.presentation.ui.components.date.FleetTimePickerDialog
import com.example.carfleetapp.presentation.ui.components.textfield.FleetTextField
import com.example.domain.model.booking.Booking
import com.example.domain.model.booking.UpdateBooking
import java.time.Instant
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateBookingDialog(
    booking: Booking,
    onDismiss: () -> Unit,
    onConfirm: (UpdateBooking) -> Unit,
    onOpenMapForFrom: () -> Unit,
    onOpenMapForTo: () -> Unit
) {
    val initialDateTime = remember(booking.departureTime) {
        try { OffsetDateTime.parse(booking.departureTime) } catch (e: Exception) { null }
    }

    var fromAddress by remember { mutableStateOf(booking.fromAddress) }
    var toAddress by remember { mutableStateOf(booking.toAddress) }
    var purpose by remember { mutableStateOf("") }
    var seats by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf(initialDateTime?.toLocalDate()) }
    var selectedTime by remember { mutableStateOf(initialDateTime?.toLocalTime()) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Text(
                "Обновить бронирование",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FleetAddressField(
                    value = fromAddress,
                    onValueChange = { fromAddress = it },
                    label = "Откуда",
                    onMapClick = onOpenMapForFrom
                )

                FleetAddressField(
                    value = toAddress,
                    onValueChange = { toAddress = it },
                    label = "Куда",
                    onMapClick = onOpenMapForTo
                )

                FleetDateTimePickerRow(
                    selectedDateText = selectedDate?.format(dateFormatter) ?: "",
                    selectedTimeText = selectedTime?.format(timeFormatter) ?: "",
                    onDateClick = { showDatePicker = true },
                    onTimeClick = { showTimePicker = true }
                )

                FleetTextField(
                    value = seats,
                    onValueChange = { seats = it },
                    label = "Количество мест",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                FleetTextField(
                    value = purpose,
                    onValueChange = { purpose = it },
                    label = "Цель поездки (Опционально)"
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newIsoDeparture = if (selectedDate != null && selectedTime != null) {
                        val offset = initialDateTime?.offset ?: ZoneOffset.UTC
                        val newDateTime = OffsetDateTime.of(selectedDate, selectedTime, offset)
                        newDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    } else null

                    val finalDeparture = if (newIsoDeparture != booking.departureTime) newIsoDeparture else null

                    val params = UpdateBooking(
                        fromAddress = fromAddress.takeIf { it != booking.fromAddress },
                        toAddress = toAddress.takeIf { it != booking.toAddress },
                        desiredDeparture = finalDeparture,
                        seats = seats.toIntOrNull(),
                        purpose = purpose.takeIf { it.isNotBlank() }
                    )
                    onConfirm(params)
                },
                shape = RoundedCornerShape(12.dp)
            ) { Text("Сохранить") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )

    if (showDatePicker) {
        FleetDatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { millis ->
                if (millis != null) {
                    selectedDate = Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()
                }
            }
        )
    }

    if (showTimePicker) {
        FleetTimePickerDialog(
            onDismiss = { showTimePicker = false },
            onTimeSelected = { hour, minute ->
                selectedTime = LocalTime.of(hour, minute)
            }
        )
    }
}
