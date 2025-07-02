package com.example.vladivostokcityguide.data.network

import com.example.vladivostokcityguide.data.Resource
import com.example.vladivostokcityguide.data.dto.LandmarkDto
import com.example.vladivostokcityguide.data.dto.route.RouteDto
import com.example.vladivostokcityguide.domain.PlaceCategory

interface RemoteDataSource {
    suspend fun getPlacesByCategory(placeCategory: PlaceCategory): List<LandmarkDto>
    suspend fun getTimeAndDistance(
        startLatitude: Double,
        startLongitude: Double,
        endLatitude: Double,
        endLongitude: Double
    ): RouteDto?
}