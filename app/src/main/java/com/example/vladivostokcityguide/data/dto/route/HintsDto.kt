package com.example.vladivostokcityguide.data.dto.route

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HintsDto(
    @SerialName("visited_nodes.sum")
    val visitedNodesSum: Double,
    @SerialName("visited_nodes.average")
    val visitedNodesAverage: Double
)

