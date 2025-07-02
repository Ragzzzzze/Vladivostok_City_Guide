package com.example.vladivostokcityguide.data.dto.route

import kotlinx.serialization.Serializable

@Serializable
data class RouteDto(
    val hints: HintsDto,
    val info: InfoDto,
    val paths: List<PathDto>
)
