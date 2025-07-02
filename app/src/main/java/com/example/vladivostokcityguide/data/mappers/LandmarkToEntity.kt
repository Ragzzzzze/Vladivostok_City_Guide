package com.example.vladivostokcityguide.data.mappers

import com.example.vladivostokcityguide.data.local.entitity.LandmarkEntity
import com.example.vladivostokcityguide.domain.models.Landmark

fun Landmark.toEntity(): LandmarkEntity {
    return LandmarkEntity(
        date = System.currentTimeMillis(),
        landmark = this
    )
}