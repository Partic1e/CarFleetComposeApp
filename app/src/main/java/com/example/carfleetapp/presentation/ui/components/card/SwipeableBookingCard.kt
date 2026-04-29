package com.example.carfleetapp.presentation.ui.components.card

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.domain.model.booking.Booking
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

enum class DragAnchors {
    Start,
    End
}

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwipeableBookingCard(
    booking: Booking,
    onDelete: (String) -> Unit,
    onUpdate: (Booking) -> Unit
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val actionsWidthDp = 140.dp
    val actionsWidthPx = with(density) { actionsWidthDp.toPx() }

    val decaySpec = rememberSplineBasedDecay<Float>()

    val anchors = remember(actionsWidthPx) {
        DraggableAnchors {
            DragAnchors.Start at 0f
            DragAnchors.End at -actionsWidthPx
        }
    }

    val state = remember(anchors) {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            anchors = anchors,
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            snapAnimationSpec = tween(durationMillis = 300),
            decayAnimationSpec = decaySpec
        )
    }

    val currentOffset = state.offset
    val safeOffset = if (currentOffset != currentOffset) 0f else currentOffset

    val dragProgress = if (actionsWidthPx > 0) {
        (abs(safeOffset) / actionsWidthPx).coerceIn(0f, 1f)
    } else 0f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .width(actionsWidthDp)
                .fillMaxHeight()
                .padding(end = 16.dp)
                .graphicsLayer { alpha = dragProgress },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalIconButton(
                onClick = {
                    scope.launch { state.animateTo(DragAnchors.Start) }
                    onUpdate(booking)
                },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Icon(Icons.Default.Edit, contentDescription = "Изменить")
            }

            FilledTonalIconButton(
                onClick = {
                    scope.launch { state.animateTo(DragAnchors.Start) }
                    onDelete(booking.id)
                },
                colors = IconButtonDefaults.filledTonalIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Удалить")
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = safeOffset.roundToInt(),
                        y = 0
                    )
                }
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Horizontal,
                    flingBehavior = AnchoredDraggableDefaults.flingBehavior(state)
                )
        ) {
            BookingCard(booking)
        }
    }
}
