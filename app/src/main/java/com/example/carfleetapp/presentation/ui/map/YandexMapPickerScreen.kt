package com.example.carfleetapp.presentation.ui.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.ColorInt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.carfleetapp.R
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.BusinessObjectMetadata
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session
import com.yandex.mapkit.search.SuggestItem
import com.yandex.mapkit.search.SuggestOptions
import com.yandex.mapkit.search.SuggestResponse
import com.yandex.mapkit.search.SuggestSession
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider

fun createBitmapFromVector(context: Context, art: Int, @ColorInt tintColor: Int): Bitmap? {
    val drawable = ContextCompat.getDrawable(context, art) ?: return null

    val width = (drawable.intrinsicWidth * 2.5).toInt()
    val height = (drawable.intrinsicHeight * 2.5).toInt()

    val bitmap = createBitmap(width, height)
    val canvas = Canvas(bitmap)

    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.setTint(tintColor)
    drawable.draw(canvas)

    return bitmap
}

fun createBlueDotBitmap(context: Context): Bitmap {
    val size = (20 * context.resources.displayMetrics.density).toInt()
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)
    val paint = android.graphics.Paint().apply {
        color = "#0EA5E9".toColorInt()
        isAntiAlias = true
    }

    canvas.drawCircle(size / 2f, size / 2f, size / 2f, android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        isAntiAlias = true
    })
    canvas.drawCircle(size / 2f, size / 2f, size / 2f - 4, paint)
    return bitmap
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YandexMapPickerScreen(
    onLocationSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val primaryColorInt = MaterialTheme.colorScheme.primary.toArgb()
    val accuracyCircleColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f).toArgb()

    var searchQuery by remember { mutableStateOf("") }
    var currentAddress by remember { mutableStateOf("Выберите точку на карте") }
    var isMarkerPlaced by remember { mutableStateOf(false) }
    var suggestions by remember { mutableStateOf<List<SuggestItem>>(emptyList()) }

    val mapView = remember { MapView(context) }
    var placemark by remember { mutableStateOf<PlacemarkMapObject?>(null) }

    val searchManager = remember { SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED) }
    var searchSession by remember { mutableStateOf<Session?>(null) }
    var suggestSession by remember { mutableStateOf<SuggestSession?>(null) }

    val userLocationLayer: UserLocationLayer = remember {
        MapKitFactory.getInstance().createUserLocationLayer(mapView.mapWindow).apply {
            isVisible = true
        }
    }

    val locationListener = remember {
        object : UserLocationObjectListener {
            override fun onObjectAdded(view: UserLocationView) {
                val blueDot = ImageProvider.fromBitmap(createBlueDotBitmap(context))
                view.arrow.setIcon(blueDot)
                view.pin.setIcon(blueDot)
                view.accuracyCircle.fillColor = accuracyCircleColor
            }
            override fun onObjectRemoved(p0: UserLocationView) {}
            override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {}
        }
    }

    val suggestListener = object : SuggestSession.SuggestListener {
        override fun onResponse(response: SuggestResponse) {
            suggestions = response.items
        }

        override fun onError(error: Error) {
            suggestions = emptyList()
        }
    }

    fun requestSuggest(query: String) {
        if (query.length < 3) {
            suggestions = emptyList()
            return
        }

        val visibleRegion = mapView.map.visibleRegion
        val boundingBox = com.yandex.mapkit.geometry.BoundingBox(
            visibleRegion.bottomLeft,
            visibleRegion.topRight
        )

        suggestSession = searchManager.createSuggestSession()
        suggestSession?.suggest(
            query,
            boundingBox,
            SuggestOptions().apply { suggestTypes = SearchType.GEO.value },
            suggestListener
        )
    }

    fun searchAndSnap(point: Point?, text: String? = null) {
        searchSession?.cancel()
        suggestions = emptyList()

        val searchOptions = SearchOptions().apply {
            searchTypes = SearchType.GEO.value
            resultPageSize = 1
            userPosition = userLocationLayer.cameraPosition()?.target
        }

        val searchListener = object : Session.SearchListener {
            override fun onSearchResponse(response: Response) {
                val result = response.collection.children.firstOrNull()?.obj
                val geometry = result?.geometry?.firstOrNull()?.point

                if (geometry != null) {
                    val toponymMeta = result.metadataContainer.getItem(ToponymObjectMetadata::class.java)
                    val businessMeta = result.metadataContainer.getItem(BusinessObjectMetadata::class.java)

                    val fullAddress = toponymMeta?.address?.formattedAddress
                        ?: businessMeta?.address?.formattedAddress

                    isMarkerPlaced = true

                    currentAddress = fullAddress
                        ?: result.descriptionText?.let { "${result.name}, $it" }
                                ?: result.name
                                ?: "Адрес найден"

                    if (placemark == null) {
                        val bitmap = createBitmapFromVector(context, R.drawable.ic_location_pin, primaryColorInt)
                        bitmap?.let {
                            placemark = mapView.map.mapObjects.addPlacemark(geometry, ImageProvider.fromBitmap(it))
                        }
                    } else {
                        placemark?.geometry = geometry
                    }

                    mapView.map.move(
                        CameraPosition(geometry, 17f, 0f, 0f),
                        Animation(Animation.Type.SMOOTH, 0.5f),
                        null
                    )
                }
            }
            override fun onSearchError(error: Error) {}
        }

        if (point != null) {
            searchManager.submit(point, 17, searchOptions, searchListener)
        } else if (!text.isNullOrBlank()) {
            val region = VisibleRegionUtils.toPolygon(mapView.map.visibleRegion)
            searchManager.submit(text, region, searchOptions, searchListener)
        }
    }

    val inputListener = remember {
        object : InputListener {
            override fun onMapTap(map: Map, point: Point) { searchAndSnap(point) }
            override fun onMapLongTap(map: Map, point: Point) {}
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    MapKitFactory.getInstance().onStart()
                    mapView.onStart()
                    userLocationLayer.setObjectListener(locationListener)
                    mapView.map.addInputListener(inputListener)
                }
                Lifecycle.Event.ON_STOP -> {
                    mapView.onStop()
                    MapKitFactory.getInstance().onStop()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(top = 14.dp)
                .align(Alignment.TopCenter)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        requestSuggest(it)
                    },
                    placeholder = { Text("Поиск...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { searchAndSnap(null, searchQuery) }) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }

            if (suggestions.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .heightIn(max = 280.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    LazyColumn {
                        items(suggestions) { item ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        searchQuery = item.displayText ?: ""
                                        searchAndSnap(null, item.searchText)
                                    }
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = item.title.text,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                item.subtitle?.let {
                                    Text(
                                        text = it.text,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }

        SmallFloatingActionButton(
            onClick = {
                userLocationLayer.cameraPosition()?.let {
                    mapView.map.move(CameraPosition(it.target, 17f, 0f, 0f), Animation(Animation.Type.SMOOTH, 0.5f), null)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 185.dp, end = 16.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Icon(Icons.Default.MyLocation, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }

        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Подтвердите выбор адреса", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(currentAddress, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onLocationSelected(currentAddress) },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    enabled = isMarkerPlaced,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Подтвердить", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
