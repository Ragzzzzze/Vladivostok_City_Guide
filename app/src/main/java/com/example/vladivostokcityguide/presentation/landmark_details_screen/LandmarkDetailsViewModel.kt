package com.example.vladivostokcityguide.presentation.landmark_details_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.vladivostokcityguide.data.Resource
import com.example.vladivostokcityguide.domain.CityGuideRepository
import com.example.vladivostokcityguide.domain.models.Landmark
import com.example.vladivostokcityguide.presentation.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class LandmarkDetailsViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CityGuideRepository
) : ViewModel() {
    var state by mutableStateOf(LandmarkDetailsScreenState())

    init {
        val jsonLandmark = savedStateHandle.toRoute<Destination.LandmarkDetailsScreen>().landmark
        val landmark = Json.decodeFromString<Landmark>(jsonLandmark)
        state = state.copy(
            landmark = landmark
        )
        getLandmarkRoute(
            endLatitude = landmark.latitude,
            endLongitude = landmark.longitude
        )
    }
    fun onEvent(event: LandmarkDetailsScreenEvent) {
        when (event) {
            is LandmarkDetailsScreenEvent.ShowFact -> {
                state = state.copy(factToShow = event.fact)
            }

            LandmarkDetailsScreenEvent.ToggleSave -> {
                state = state.copy(
                    landmark = state.landmark!!.copy(isSaved = !state.landmark!!.isSaved)
                )
                if (state.landmark!!.isSaved) {
                    saveLandmark(state.landmark!!)
                } else {
                    deleteLandmark(state.landmark!!)
                }
            }

            LandmarkDetailsScreenEvent.CloseFact -> {
                state = state.copy(factToShow = null)
            }
        }
    }
    private fun getLandmarkRoute(endLatitude: Double, endLongitude: Double) {
        viewModelScope.launch {
            val route = repository.getRoute(
                endLatitude = endLatitude,
                endLongitude = endLongitude
            )
            state = when (route) {
                is Resource.Success -> {
                    state.copy(route = route.data)
                }

                is Resource.Error -> {
                    state.copy(error = route.message)
                }
            }
        }
    }

    private fun saveLandmark(landmark: Landmark){
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveLandmark(landmark)
        }
    }

    private fun deleteLandmark(landmark: Landmark) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLandmark(landmark)
        }
    }
}