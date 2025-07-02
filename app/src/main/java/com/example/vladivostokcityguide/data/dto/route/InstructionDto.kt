package com.example.vladivostokcityguide.data.dto.route

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InstructionDto(
    val distance: Double,
    val heading: Double? = null,
    val sign: Int,
    val interval: List<Int>,
    val text: String,
    val time: Long,
    @SerialName("street_name")
    val streetName: String,
    @SerialName("last_heading")
    val lastHeading: Double? = null
)
