package com.example.vladivostokcityguide.data.repository

import android.util.Log
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.vladivostokcityguide.data.local.dao.CityGuideDao
import com.example.vladivostokcityguide.data.Resource
import com.example.vladivostokcityguide.data.dto.LandmarkDto
import com.example.vladivostokcityguide.data.location.LocationDataSource
import com.example.vladivostokcityguide.data.mappers.toDomain
import com.example.vladivostokcityguide.data.mappers.toEntity
import com.example.vladivostokcityguide.data.network.RemoteDataSource
import com.example.vladivostokcityguide.domain.CityGuideRepository
import com.example.vladivostokcityguide.domain.Order
import com.example.vladivostokcityguide.domain.PlaceCategory
import com.example.vladivostokcityguide.domain.models.Landmark
import com.example.vladivostokcityguide.domain.models.Point
import com.example.vladivostokcityguide.domain.models.Route
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.supervisorScope

class CityGuideRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val locationDataSource: LocationDataSource,
    private val dao: CityGuideDao
) : CityGuideRepository {
    override suspend fun getPlacesByCategory(placeCategory: PlaceCategory): Resource<List<Landmark>> {
        return try {
            val currentLocation = locationDataSource.getCurrentLocation()
            val data = remoteDataSource.getPlacesByCategory(placeCategory).map {
                val (time, distance) = locationDataSource.getTimeAndDistance(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    it.latitude,
                    it.longitude
                )
                val isSaved = dao.isLandmarkSaved(
                    it.latitude, it.longitude, it.name
                )
                it.toDomain(time, distance, isSaved)
            }
            Resource.Success(data)
        } catch (e: Exception) {
            Log.e("CityGuideRepositoryImpl", "Error fetching route: ${e.localizedMessage}")
            Resource.Error(e.localizedMessage ?: "Unexpected error")
        }
    }

    override suspend fun getRoute(
        endLatitude: Double,
        endLongitude: Double
    ): Resource<Route> {
        val currentLocation = locationDataSource.getCurrentLocation()
        return try {
            val data = remoteDataSource.getTimeAndDistance(
                currentLocation.latitude, currentLocation.longitude, endLatitude, endLongitude
            )?.toDomain()
            if (data == null) {
                return Resource.Error("No route found")
            }
            Resource.Success(data)
        } catch (e: Exception) {
            Log.e("CityGuideRepositoryImpl", "Error fetching route: ${e.localizedMessage}")
            Resource.Error(e.localizedMessage ?: "Unexpected error")
        }
    }

    override suspend fun getCurrentUserLocationFlow(): Flow<Point> {
        return locationDataSource.getCurrentLocationFlow()
    }

    override suspend fun getAllLandmarks(): Resource<List<Landmark>> = try {
        val categories = PlaceCategory.entries
        val currentLoc = locationDataSource.getCurrentLocation()

        val dtosPerCategory: List<List<LandmarkDto>> = supervisorScope {
            categories.map { category ->
                async {
                    runCatching {
                        remoteDataSource.getPlacesByCategory(category)
                    }.getOrElse { throwable ->
                        Log.e(
                            "CityGuideRepo",
                            "Error loading $category: ${throwable.localizedMessage}"
                        )
                        emptyList()
                    }
                }
            }.awaitAll()
        }

        val domainLandmarks = dtosPerCategory
            .flatten()
            .map { dto ->
                val (time, dist) = locationDataSource.getTimeAndDistance(
                    currentLoc.latitude, currentLoc.longitude,
                    dto.latitude, dto.longitude
                )
                val isSaved = dao.isLandmarkSaved(
                    dto.latitude, dto.longitude, dto.name
                )
                dto.toDomain(time, dist, isSaved)
            }

        Resource.Success(domainLandmarks)
    } catch (e: Exception) {
        Resource.Error(e.localizedMessage ?: "Unexpected error")
    }

    override suspend fun saveLandmark(landmark: Landmark) {
        dao.saveLandmark(landmark.toEntity())
    }

    override suspend fun deleteLandmark(landmark: Landmark) {
        dao.deleteLandmark(landmark.latitude, landmark.longitude, landmark.name)
    }

    override suspend fun isLandmarkSaved(lat: Double, lng: Double, name: String): Boolean {
        return dao.isLandmarkSaved(
            lat, lng, name
        )
    }

    override fun getSavedLandmarks(): Flow<List<Landmark>> {
        return dao.getSavedLandmarks()
    }

    override fun getSortedLandmarks(
        field: String,
        order: Order
    ): Flow<List<Landmark>> {
        val sortOrder = if (order == Order.ASCENDING) "ASC" else "DESC"
        val query = SimpleSQLiteQuery("SELECT * FROM LandmarkEntity ORDER BY `$field` $sortOrder")
        return dao.getSortedLandmarks(query)
    }

}