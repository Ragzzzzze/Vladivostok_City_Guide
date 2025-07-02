package com.example.vladivostokcityguide.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val timeMin: Int,
    // В метрах
    val distance: Int,
    val coordinates: List<Point>,
)
