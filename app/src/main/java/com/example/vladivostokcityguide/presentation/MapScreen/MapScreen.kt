package com.example.vladivostokcityguide.presentation.MapScreen

import android.content.pm.PackageManager
import android.os.Looper
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    attractionId: Int,
    onBackClick: () -> Unit
) {
    val vladivostok = LatLng(43.1056, 131.874)
    val fefu = LatLng(43.025156, 131.893899)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(vladivostok, 10f)
    }


    var userLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }

    LocationTracker { newLocation ->
        userLocation = newLocation
    }

    val mapProperties = MapProperties(
        isMyLocationEnabled = true
    )


    LaunchedEffect(userLocation) {
        cameraPositionState.animate(
            CameraUpdateFactory.newLatLngZoom(userLocation, 15f)
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties,
        onMyLocationButtonClick = {false}
    ){
        Marker(
            state = rememberMarkerState(position = fefu),
            draggable = false,
            title = "FEFU",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
        )

        Marker(
            state = MarkerState(position = userLocation),
            title = "Your location",
            draggable = false,
        )


    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission() {
    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )
    )

    LaunchedEffect(key1 = locationPermissions.permissions) {
        locationPermissions.launchMultiplePermissionRequest()
    }
}


@Composable
fun LocationTracker(
    onLocationUpdate: (LatLng) -> Unit
) {
    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    onLocationUpdate(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }
    DisposableEffect(Unit) {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }

        if (ContextCompat.checkSelfPermission(
                context,
                "android.permission.ACCESS_FINE_LOCATION"
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}