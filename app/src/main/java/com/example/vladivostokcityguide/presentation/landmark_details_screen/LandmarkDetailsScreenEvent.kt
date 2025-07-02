package com.example.vladivostokcityguide.presentation.landmark_details_screen

sealed interface LandmarkDetailsScreenEvent {
    data class ShowFact(val fact: String) : LandmarkDetailsScreenEvent
    data object ToggleSave : LandmarkDetailsScreenEvent
    data object CloseFact : LandmarkDetailsScreenEvent
}