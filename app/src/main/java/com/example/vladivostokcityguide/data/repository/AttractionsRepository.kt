package com.example.vladivostokcityguide.data.repository

import com.example.vladivostokcityguide.data.model.Attraction
import com.example.vladivostokcityguide.R

class AttractionsRepository {
    fun getAttractionsByCategory(category: String): List<Attraction> {
        return when (category) {
            "Theaters" -> listOf(
                Attraction(1, "Mariinsky", "0.8km away", "10 mins", R.drawable.mariinsky, "Theaters"),
                Attraction(2, "Gorky Theater", "1.2km away", "15 mins", R.drawable.theater, "Theaters")
            )
            "Statues" -> listOf(
                Attraction(3, "Statue of Pushkin", "2.8km away", "32 mins", R.drawable.pushkin, "Statues"),
                Attraction(4, "Statue of Vysockiy", "3.5km away", "40 mins", R.drawable.vysockiy, "Statues")
            )
            else -> listOf(
                Attraction(5, "Square", "1.3km away", "15 mins", R.drawable.square, "Others"),
                Attraction(6, "Statue of tiger", "5.2km away", "45 mins", R.drawable.tiger, "Others"),
                Attraction(7, "Kolobok", "3.1km away", "25 mins", R.drawable.kolobok, "Others"),
                Attraction(8, "Beacon", "3.1km away", "25 mins", R.drawable.beacon, "Others")
            )
        }
    }

    fun getCategories(): List<String> {
        return listOf("Theaters", "Statues", "Museums", "Others")
    }
}