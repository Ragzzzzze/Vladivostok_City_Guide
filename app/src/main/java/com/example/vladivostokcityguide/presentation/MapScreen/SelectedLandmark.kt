package com.example.vladivostokcityguide.presentation.MapScreen

import com.example.vladivostokcityguide.domain.models.Landmark
import com.example.vladivostokcityguide.domain.models.Route

data class SelectedLandmark(
    val landmark: Landmark,
    val route: Route? = null,
    val isShowRoute: Boolean = false,
)
