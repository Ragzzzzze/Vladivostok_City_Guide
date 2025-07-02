package com.example.vladivostokcityguide.domain

import androidx.annotation.StringRes
import com.example.vladivostokcityguide.R

enum class PlaceCategory(val apiValue: String, @StringRes val nameId: Int){
    THEATER("theater", R.string.category_theater),
    STATUE("statue", R.string.category_statue),
    MUSEUM("museum", R.string.category_museums),
    OTHER("other", R.string.category_others);

    companion object {
        fun fromApiValue(value: String): PlaceCategory? {
            return entries.find { it.apiValue == value }
        }
    }
}


