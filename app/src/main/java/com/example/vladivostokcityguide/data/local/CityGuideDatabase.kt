package com.example.vladivostokcityguide.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.vladivostokcityguide.data.local.dao.CityGuideDao
import com.example.vladivostokcityguide.data.local.entitity.LandmarkEntity

@Database(
    entities = [LandmarkEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class CityGuideDatabase: RoomDatabase() {
    abstract val cityGuideDao: CityGuideDao
}