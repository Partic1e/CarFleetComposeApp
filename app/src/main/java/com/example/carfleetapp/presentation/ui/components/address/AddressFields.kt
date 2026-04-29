package com.example.carfleetapp.presentation.ui.components.address

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FleetAddressField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    onMapClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        trailingIcon = if (onMapClick != null) {
            {
                IconButton(onClick = onMapClick) {
                    Icon(imageVector = Icons.Default.Map, contentDescription = "Выбрать на карте")
                }
            }
        } else null
    )
}
