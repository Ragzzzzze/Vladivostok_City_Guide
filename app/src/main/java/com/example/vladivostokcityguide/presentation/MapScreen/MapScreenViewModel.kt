package com.example.vladivostokcityguide.presentation.MapScreen


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
import com.example.vladivostokcityguide.domain.models.Route
import com.example.vladivostokcityguide.presentation.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class MapScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val repository: CityGuideRepository
) : ViewModel() {
    var state by mutableStateOf(MapScreenState())

    init {
        getCurrentUserLocationFlow()
        loadAllLandMarks()
        val jsonLandmark = savedStateHandle.toRoute<Destination.MapScreen>().landmarkJson
        val jsonRoute = savedStateHandle.toRoute<Destination.MapScreen>().routeJson
        if (jsonLandmark != null && jsonRoute != null) {
            state = state.copy(
                selectedLandmark = SelectedLandmark(
                    landmark = Json.decodeFromString<Landmark>(jsonLandmark),
                    route = Json.decodeFromString<Route>(jsonRoute)
                )
            )
        }
    }

    fun onEvent(event: MapScreenEvent) {
        when (event) {
            MapScreenEvent.ToggleSaveLandmark -> {
                val selectedLandmark = state.selectedLandmark
                state = state.copy(
                    selectedLandmark = selectedLandmark!!.copy(
                        isSaved = !selectedLandmark.isSaved
                    )
                )
            }

            MapScreenEvent.UnselectLandmark -> {
                state = state.copy(
                    selectedLandmark = null
                )
            }

            MapScreenEvent.ToggleShowRoute -> {
                val selectedLandmark = state.selectedLandmark
                state = state.copy(
                    selectedLandmark = selectedLandmark!!.copy(
                        isShowRoute = !selectedLandmark.isShowRoute
                    )
                )
            }

            is MapScreenEvent.SelectLandmark -> {
                val landmark = event.landmark
                getLandmarkRoute(landmark.latitude, landmark.longitude)
                state = state.copy(
                    selectedLandmark = SelectedLandmark(
                        landmark = landmark,
                    )
                )
            }
        }
    }

    private fun getLandmarkRoute(endLatitude: Double, endLongitude: Double) =
        viewModelScope.launch {
            val result = repository.getRoute(
                endLatitude = endLatitude,
                endLongitude = endLongitude
            )
            when (result) {
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message
                    )
                }

                is Resource.Success -> {
                    val route = result.data
                    state = state.copy(
                        selectedLandmark = state.selectedLandmark?.copy(route = route)
                    )
                }
            }
        }
    private fun getCurrentUserLocationFlow() {
        viewModelScope.launch {
            repository.getCurrentUserLocationFlow().collect {
                state = state.copy(
                    currentLocation = it
                )
            }
        }
    }
    private fun loadAllLandMarks() {
        viewModelScope.launch {
            val result = repository.getAllLandmarks()
            when (result) {
                is Resource.Error -> {
                    state = state.copy(
                        error = result.message
                    )
                }

                is Resource.Success -> {
                    val landmarks = result.data
                    state = state.copy(
                        landmarks = landmarks
                    )
                }
            }
        }
    }
}