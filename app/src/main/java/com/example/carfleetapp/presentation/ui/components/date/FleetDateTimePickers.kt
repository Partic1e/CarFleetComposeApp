package com.example.carfleetapp.presentation.ui.components.date

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.carfleetapp.presentation.ui.theme.FleetManagementTheme
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatIsoDate(isoString: String): String {
    return try {
        val parsed = OffsetDateTime.parse(isoString)
        parsed.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    } catch (e: Exception) {
        isoString
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetDatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (Long?) -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val context = LocalContext.current

    val config = Configuration(LocalConfiguration.current).apply {
        setLocale(Locale("ru", "RU"))
    }
    val localizedContext = context.createConfigurationContext(config)

    CompositionLocalProvider(LocalContext provides localizedContext) {
        FleetManagementTheme {
            Dialog(
                onDismissRequest = onDismiss,
                properties = DialogProperties(usePlatformDefaultWidth = true)
            ) {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        DatePicker(
                            state = datePickerState,
                            colors = DatePickerDefaults.colors(
                                containerColor = Color.Transparent,
                                titleContentColor = MaterialTheme.colorScheme.primary,
                                headlineContentColor = MaterialTheme.colorScheme.onSurface,
                                selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                                selectedDayContentColor = Color.White,
                                todayContentColor = MaterialTheme.colorScheme.primary,
                                todayDateBorderColor = MaterialTheme.colorScheme.primary,
                                dayContentColor = MaterialTheme.colorScheme.onSurface,
                                weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            showModeToggle = true
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text("Отмена", color = MaterialTheme.colorScheme.secondary)
                            }
                            Spacer(Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    onDateSelected(datePickerState.selectedDateMillis)
                                    onDismiss()
                                }
                            ) {
                                Text("ОК", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FleetTimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    val timePickerState = rememberTimePickerState(is24Hour = true)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(timePickerState.hour, timePickerState.minute)
                onDismiss()
            }) {
                Text("ОК", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        text = {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = MaterialTheme.colorScheme.surfaceVariant,
                    clockDialSelectedContentColor = Color.White,
                    clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurface,
                    selectorColor = MaterialTheme.colorScheme.primary,
                    timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    timeSelectorSelectedContentColor = MaterialTheme.colorScheme.primary,
                    timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    )
}

@Composable
fun FleetDateTimePickerRow(
    selectedDateText: String,
    selectedTimeText: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val textFieldStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)

        OutlinedTextField(
            value = selectedDateText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Дата", fontSize = 12.sp) },
            modifier = Modifier.weight(1.1f),
            textStyle = textFieldStyle,
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = onDateClick) {
                    Icon(Icons.Default.DateRange, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }
        )
        OutlinedTextField(
            value = selectedTimeText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Время", fontSize = 12.sp) },
            modifier = Modifier.weight(0.9f),
            textStyle = textFieldStyle,
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = onTimeClick) {
                    Icon(Icons.Default.Schedule, null, Modifier.size(20.dp), tint = MaterialTheme.colorScheme.primary)
                }
            }
        )
    }
}
