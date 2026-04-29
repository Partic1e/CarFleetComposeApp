package com.example.carfleetapp.presentation.ui.driver.list

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.carfleetapp.presentation.ui.components.card.TripSlotCard
import com.example.domain.model.tripslot.Waypoint
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DriverTripSlotsScreen(
    onOpenNavigator: (List<Waypoint>) -> Unit,
    viewModel: DriverTripSlotsViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadSlots()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Мои рейсы",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (viewModel.errorMessage != null) {
                Text(
                    text = viewModel.errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp),
                    textAlign = TextAlign.Center
                )
            } else if (viewModel.slots.isEmpty()) {
                EmptyTripsState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.slots) { slot ->
                        TripSlotCard(
                            slot = slot,
                            onNavigateClick = {
                                val pointsToNavigate = slot.waypoints.ifEmpty {
                                    listOf(
                                        Waypoint(
                                            "",
                                            slot.fromAddress,
                                            "Pickup",
                                            slot.departureTime,
                                            slot.fromLat,
                                            slot.fromLon
                                        ),
                                        Waypoint(
                                            "",
                                            slot.toAddress,
                                            "Dropoff",
                                            slot.arrivalPlanned ?: "",
                                            slot.toLat,
                                            slot.toLon
                                        )
                                    )
                                }
                                onOpenNavigator(pointsToNavigate)
                            }
                        )
                    }
                }
            }
        }
    }
}
