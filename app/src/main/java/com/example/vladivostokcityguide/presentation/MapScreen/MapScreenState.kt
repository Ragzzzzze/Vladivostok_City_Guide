package com.example.vladivostokcityguide.presentation.MapScreen

import com.example.vladivostokcityguide.domain.models.Landmark
import com.example.vladivostokcityguide.domain.models.Point
import com.example.vladivostokcityguide.domain.models.Route

data class MapScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isMapReady: Boolean = false,
    val currentLocation: Point? = null,
    val selectedLandmark: SelectedLandmark? = null,
    val landmarks: List<Landmark> = emptyList(),
)
