package com.example.vladivostokcityguide.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

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