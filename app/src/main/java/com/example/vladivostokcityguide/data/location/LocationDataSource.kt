package com.example.vladivostokcityguide.data.location

import com.example.vladivostokcityguide.domain.models.Point
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
    suspend fun getCurrentLocation(): Point
    suspend fun getCurrentLocationFlow(): Flow<Point>
    suspend fun getTimeAndDistance(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double,
    ): Pair<Int, Int>
}