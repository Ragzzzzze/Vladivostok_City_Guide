package com.example.vladivostokcityguide.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Landmark(
    val name: String,
    val rating: Double,
    val description: String,
    val imgUrl: String,
    val facts: List<String>,
    val latitude: Double,
    val longitude: Double,
    val time: Int,
    val distance: Int,
    val isSaved: Boolean
)
