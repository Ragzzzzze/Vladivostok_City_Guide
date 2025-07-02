package com.example.vladivostokcityguide.data.dto.route

import kotlinx.serialization.Serializable

@Serializable
data class InfoDto(
    val copyrights: List<String>,
    val took: Int
)