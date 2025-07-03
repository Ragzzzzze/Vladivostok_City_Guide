package com.example.vladivostokcityguide.presentation.MapScreen

import android.annotation.SuppressLint
import android.view.Surface
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.vladivostokcityguide.R
import com.example.vladivostokcityguide.app.ui.theme.NeonGreen
import com.example.vladivostokcityguide.presentation.MapScreen.components.LandmarkBottomSheetScaffold
import com.example.vladivostokcityguide.presentation.navigation.Destination
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState


@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    navController: NavController,
    onEvent: (MapScreenEvent) -> Unit,
    state: MapScreenState,
) {
    val context = LocalContext.current
    // Загружаем стиль из файла dark_map.json, который должен находиться в res/raw
    val darkMapStyle = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_theme)
    val mapProperties = MapProperties(
        isMyLocationEnabled = true,
        mapStyleOptions = darkMapStyle
    )

    val vladivostok = LatLng(43.1056, 131.874)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(vladivostok, 10f)
    }


    var userLocation by remember { mutableStateOf(state.currentLocation) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp


    LaunchedEffect(userLocation) {
        userLocation?.let { (latitude, longitude) ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 15f)
            )
        }
    }

    val selectedLandmark = state.selectedLandmark
    LaunchedEffect(selectedLandmark) {
        selectedLandmark?.let {
            val target = LatLng(it.landmark.latitude, it.landmark.longitude)
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(target, 15f)
            )
        }
    }
    LandmarkBottomSheetScaffold(
        selectedLandmark = selectedLandmark,
        onDismiss = { onEvent(MapScreenEvent.UnselectLandmark) },
        onNavigateToDetails = { landmarkJson ->
            navController.navigate(
                Destination.LandmarkDetailsScreen(landmarkJson)
            )
        },
        onToggleShowRoute = { onEvent(MapScreenEvent.ToggleShowRoute) },
        isLandmarkSaved = selectedLandmark?.landmark?.isSaved ?: false,
        onToggleSave = { onEvent(MapScreenEvent.ToggleSaveLandmark) },
    ) { paddingValues ->
        val customPadding = PaddingValues(
            start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
            top = 0.dp,
            end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
            bottom = paddingValues.calculateBottomPadding()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .zIndex(1f)
                .padding(start = 12.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Surface(
                shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp),
                color = NeonGreen.copy(alpha = 0.3f),
                onClick = { navController.navigateUp() }
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close icon",
                        tint = NeonGreen
                    )
                }
            }

        }
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(customPadding),
            cameraPositionState = cameraPositionState,
            properties = mapProperties,
            contentPadding = PaddingValues(bottom = screenHeight / 2),
//        onMyLocationButtonClick = {false}
        ) {
            if (state.landmarks.isNotEmpty()) {
                state.landmarks.forEach { landmark ->
                    val position = LatLng(landmark.latitude, landmark.longitude)

                    val landmarkIconColor = if (state.selectedLandmark != null && state.selectedLandmark.landmark == landmark) {
                        BitmapDescriptorFactory.HUE_GREEN
                    } else {
                        BitmapDescriptorFactory.HUE_BLUE
                    }
                    Marker(
                        state = rememberMarkerState(position = position),
                        draggable = false,
                        onClick = {
                            onEvent(MapScreenEvent.SelectLandmark(landmark))
                            true
                        },
                        title = "landmark marker",
                        icon = BitmapDescriptorFactory.defaultMarker(landmarkIconColor)
                    )
                }
            }
            val isShowRoute =
                (selectedLandmark != null) && (selectedLandmark.route != null) && (selectedLandmark.isShowRoute) && (selectedLandmark.route.coordinates.isNotEmpty())
            if (isShowRoute) {
                val routePoints = selectedLandmark.route.coordinates.map {
                    LatLng(it.latitude, it.longitude)
                }
                Polyline(
                    points = routePoints,
                    color = Color.Green,
                    width = 17f
                )
            }
            userLocation?.let { (latitude, longitude) ->
                Marker(
                    state = MarkerState(position = LatLng(latitude, longitude)),
                    title = "Your location",
                    draggable = false,
                )
            }
        }

    }
}




//    DisposableEffect(Unit) {
//        val locationRequest = LocationRequest.create().apply {
//            interval = 10000
//            fastestInterval = 5000
//            priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
//        }
//
//        if (ContextCompat.checkSelfPermission(
//                context,
//                "android.permission.ACCESS_FINE_LOCATION"
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            fusedLocationClient.requestLocationUpdates(
//                locationRequest,
//                locationCallback,
//                Looper.getMainLooper()
//            )
//        }
//
//        onDispose {
//            fusedLocationClient.removeLocationUpdates(locationCallback)
//        }
//    }