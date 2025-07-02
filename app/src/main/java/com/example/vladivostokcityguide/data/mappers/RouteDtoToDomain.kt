package com.example.vladivostokcityguide.data.mappers

import com.example.vladivostokcityguide.data.dto.route.RouteDto
import com.example.vladivostokcityguide.domain.models.Point
import com.example.vladivostokcityguide.domain.models.Route

fun RouteDto.toDomain(): Route? {
    return this.paths.firstOrNull()?.let {path->
        Route(
            timeMin = (path.time / 1000L / 60L).toInt(),
            distance = path.distance.toInt(),
            coordinates = path.points.coordinates.map { point ->
                Point(
                    latitude = point[1],
                    longitude = point[0]
                )
            }
        )
    }
}