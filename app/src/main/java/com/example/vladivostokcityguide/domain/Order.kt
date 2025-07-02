package com.example.vladivostokcityguide.domain

import androidx.annotation.StringRes
import com.example.vladivostokcityguide.R

enum class Order(@StringRes val resName: Int) {
    ASCENDING(R.string.ascending),
    DESCENDING(R.string.descending);

    fun toggle(): Order {
        return when (this) {
            ASCENDING -> DESCENDING
            DESCENDING -> ASCENDING
        }
    }
}