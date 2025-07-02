package com.example.vladivostokcityguide.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object Welcome : Destination
    @Serializable
    data class PlacesScreen(val categoryApi: String) : Destination
    @Serializable
    data class LandmarkDetailsScreen(val landmark: String) : Destination
    @Serializable
    data class MapScreen(val landmarkJson: String? = null, val routeJson: String? = null) : Destination
    @Serializable
    data object FavoriteScreen : Destination
}