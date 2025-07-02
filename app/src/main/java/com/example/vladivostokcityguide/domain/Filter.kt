package com.example.vladivostokcityguide.domain

import androidx.annotation.StringRes
import com.example.vladivostokcityguide.R

enum class Filter(@StringRes val labelRes: Int, val fieldName: String) {
    TIME(R.string.time, "time"),
    DISTANCE(R.string.distance, "distance"),
    RATING(R.string.raiting, "rating"),
    SAVING_DATE(R.string.saving_date, "date");
}