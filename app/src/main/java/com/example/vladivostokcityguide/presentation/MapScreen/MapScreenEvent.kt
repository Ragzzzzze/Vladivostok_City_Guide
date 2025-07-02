package com.example.vladivostokcityguide.presentation.MapScreen

import com.example.vladivostokcityguide.domain.models.Landmark

sealed interface MapScreenEvent {
    data class SelectLandmark(val landmark: Landmark): MapScreenEvent
    data object UnselectLandmark: MapScreenEvent
    data object ToggleShowRoute: MapScreenEvent
    data object ToggleSaveLandmark: MapScreenEvent
}