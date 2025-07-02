package com.example.vladivostokcityguide.presentation.PlacesScreen

import com.example.vladivostokcityguide.domain.Filter
import com.example.vladivostokcityguide.domain.Order
import com.example.vladivostokcityguide.domain.PlaceCategory
import com.example.vladivostokcityguide.domain.models.Landmark


data class PlacesScreenState(
    val selectedCategory: PlaceCategory? = null,
    val landmarks: List<Landmark> = emptyList(),
    val filter: Filter = Filter.RATING,
    val order: Order = Order.DESCENDING,
    val isLoading: Boolean = false,
    val error: String? = null
)