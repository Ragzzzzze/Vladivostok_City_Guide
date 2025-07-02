package com.example.vladivostokcityguide.data.dto.route

import kotlinx.serialization.Serializable

@Serializable
data class LineStringDto(
    val type: String,
    val coordinates: List<List<Double>>
)
