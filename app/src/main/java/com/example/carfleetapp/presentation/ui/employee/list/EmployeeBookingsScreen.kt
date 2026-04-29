package com.example.carfleetapp.presentation.ui.employee.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.carfleetapp.presentation.ui.components.card.SwipeableBookingCard
import com.example.carfleetapp.presentation.ui.employee.update.UpdateBookingDialog
import com.example.domain.model.booking.Booking
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeBookingsScreen(
    onOpenMapForUpdateFrom: () -> Unit,
    onOpenMapForUpdateTo: () -> Unit,
    updatedFromAddress: String?,
    updatedToAddress: String?,
    clearMapResult: () -> Unit,
    viewModel: EmployeeBookingsViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadBookings()
    }

    var bookingToUpdate by remember { mutableStateOf<Booking?>(null) }

    LaunchedEffect(updatedFromAddress) {
        if (updatedFromAddress != null && bookingToUpdate != null) {
            bookingToUpdate = bookingToUpdate!!.copy(fromAddress = updatedFromAddress)
            clearMapResult()
        }
    }

    LaunchedEffect(updatedToAddress) {
        if (updatedToAddress != null && bookingToUpdate != null) {
            bookingToUpdate = bookingToUpdate!!.copy(toAddress = updatedToAddress)
            clearMapResult()
        }
    }

    if (bookingToUpdate != null) {
        UpdateBookingDialog(
            booking = bookingToUpdate!!,
            onDismiss = { bookingToUpdate = null },
            onConfirm = { updateRequest ->
                viewModel.updateBooking(bookingToUpdate!!.id, updateRequest)
                bookingToUpdate = null
            },
            onOpenMapForFrom = onOpenMapForUpdateFrom,
            onOpenMapForTo = onOpenMapForUpdateTo
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Мои заявки",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (viewModel.bookings.isEmpty()) {
                EmptyBookingsState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.bookings, key = { it.id }) { booking ->
                        SwipeableBookingCard(
                            booking = booking,
                            onUpdate = { bookingToUpdate = it },
                            onDelete = { viewModel.cancelBooking(it) }
                        )
                    }
                }
            }
        }
    }
}
