package com.example.vladivostokcityguide.data.mappers

import com.example.vladivostokcityguide.data.dto.LandmarkDto
import com.example.vladivostokcityguide.domain.models.Landmark

fun LandmarkDto.toDomain(
    time: Int,
    distance: Int,
    isSaved: Boolean
): Landmark {
    return Landmark(
        name = name,
        rating = rating,
        description = description,
        imgUrl = imgUrl,
        facts = facts,
        latitude = latitude,
        longitude = longitude,
        time = time,
        distance = distance,
        isSaved = isSaved,
    )

}