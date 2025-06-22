package com.example.vladivostokcityguide.presentation.PlacesScreen

import com.example.vladivostokcityguide.data.model.Attraction


data class PlacesScreenState(
    val categories: List<String> = emptyList(),
    val selectedCategory: String = "",
    val attractions: List<Attraction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)