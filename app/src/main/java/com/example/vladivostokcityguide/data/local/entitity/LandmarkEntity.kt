package com.example.vladivostokcityguide.data.local.entitity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vladivostokcityguide.domain.models.Landmark

@Entity
data class LandmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0 ,
    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    val date: Long,
    @Embedded val landmark: Landmark
)
