package com.example.vladivostokcityguide.domain

import com.example.vladivostokcityguide.data.Resource
import com.example.vladivostokcityguide.data.dto.LandmarkDto
import com.example.vladivostokcityguide.domain.models.Landmark
import com.example.vladivostokcityguide.domain.models.Point
import com.example.vladivostokcityguide.domain.models.Route
import kotlinx.coroutines.flow.Flow

interface CityGuideRepository {
    suspend fun getPlacesByCategory(placeCategory: PlaceCategory): Resource<List<Landmark>>
    suspend fun getRoute(
        endLatitude: Double,
        endLongitude: Double
    ): Resource<Route>
    suspend fun getCurrentUserLocationFlow(): Flow<Point>
    suspend fun getAllLandmarks(): Resource<List<Landmark>>

    suspend fun saveLandmark(landmark: Landmark): Unit
    suspend fun deleteLandmark(landmark: Landmark): Unit
    suspend fun isLandmarkSaved(lat: Double, lng: Double, name: String): Boolean
    fun getSavedLandmarks(): Flow<List<Landmark>>
    fun getSortedLandmarks(field: String, order: Order): Flow<List<Landmark>>

}