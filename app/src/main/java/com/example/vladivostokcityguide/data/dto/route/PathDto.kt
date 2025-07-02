package com.example.vladivostokcityguide.data.dto.route

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PathDto(
    val distance: Double,
    val weight: Double,
    val time: Long,
    val transfers: Int,
    @SerialName("points_encoded")
    val pointsEncoded: Boolean,
    val bbox: List<Double>,
    val points: LineStringDto,
    val instructions: List<InstructionDto>,
    val ascend: Double,
    val descend: Double,
    @SerialName("snapped_waypoints")
    val snappedWaypoints: LineStringDto
)

