package com.example.vladivostokcityguide.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LandmarkDto(
    val name: String,
    val type: String,
    val rating: Double,
    val description: String,
    @SerialName("img") val imgUrl: String,
    val facts: List<String>,
    val latitude: Double,
    val longitude: Double
)