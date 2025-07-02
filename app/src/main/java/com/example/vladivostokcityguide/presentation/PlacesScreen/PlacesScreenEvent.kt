package com.example.vladivostokcityguide.presentation.PlacesScreen

import com.example.vladivostokcityguide.domain.Filter
import com.example.vladivostokcityguide.domain.Order
import com.example.vladivostokcityguide.domain.models.Landmark

sealed interface PlacesScreenEvent {
    data class OnFilterChange(val category: Filter) : PlacesScreenEvent
    data object OnToggleOrder : PlacesScreenEvent
    data class OnToggleSave(val landmark: Landmark) : PlacesScreenEvent
    data object OnRetry: PlacesScreenEvent
}