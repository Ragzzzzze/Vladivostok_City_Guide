package com.example.vladivostokcityguide.presentation.landmark_details_screen

import com.example.vladivostokcityguide.domain.models.Landmark
import com.example.vladivostokcityguide.domain.models.Route

data class LandmarkDetailsScreenState(
    val landmark: Landmark? = null,
    val route: Route? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val factToShow: String? = null,
)
