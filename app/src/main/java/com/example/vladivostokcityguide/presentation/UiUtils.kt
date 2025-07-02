package com.example.vladivostokcityguide.presentation

import android.annotation.SuppressLint

object UiUtils {
    @SuppressLint("DefaultLocale")
    fun formatDistance(distance: Int): String {
        return if (distance > 1000) {
            if (distance % 1000 == 0)
                "${distance / 1000} км"
            else
                String.format("%.1f км", distance / 1000f)
        } else {
            "${distance} м"
        }
    }
}