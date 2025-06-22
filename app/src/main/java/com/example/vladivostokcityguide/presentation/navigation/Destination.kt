package com.example.vladivostokcityguide.presentation.navigation


sealed class Destination(val route: String) {
    object Welcome : Destination("welcome")
    object PlacesScreen : Destination("most_popular/{category}") {
        fun createRoute(category: String) = "most_popular/$category"
    }
    object MapScreen : Destination("map_screen/{attractionId}") {
        fun createRoute(attractionId: Int) = "map_screen/$attractionId"
    }
}