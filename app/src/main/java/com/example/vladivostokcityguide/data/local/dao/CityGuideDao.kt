package com.example.vladivostokcityguide.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.vladivostokcityguide.data.local.entitity.LandmarkEntity
import com.example.vladivostokcityguide.domain.models.Landmark
import kotlinx.coroutines.flow.Flow

@Dao
interface CityGuideDao {
    @Upsert
    suspend fun saveLandmark(landmark: LandmarkEntity)

    @Query("SELECT * FROM LandmarkEntity")
    fun getSavedLandmarks(): Flow<List<Landmark>>

    @Query("DELETE FROM LandmarkEntity WHERE latitude = :lat AND longitude = :lng AND name = :name")
    suspend fun deleteLandmark(lat: Double, lng: Double, name: String)

    // В json не добавил id :(((((( мега жаль
    @Query("SELECT EXISTS(SELECT 1 FROM LandmarkEntity WHERE latitude = :lat AND longitude = :lng AND name = :name)")
    suspend fun isLandmarkSaved(lat: Double, lng: Double, name: String): Boolean

    @RawQuery(observedEntities = [LandmarkEntity::class])
    fun getSortedLandmarks(query: SupportSQLiteQuery): Flow<List<Landmark>>
}