package com.example.vladivostokcityguide.presentation.PlacesScreen


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.vladivostokcityguide.data.Resource
import com.example.vladivostokcityguide.domain.CityGuideRepository
import com.example.vladivostokcityguide.domain.Filter
import com.example.vladivostokcityguide.domain.Order
import com.example.vladivostokcityguide.domain.PlaceCategory
import com.example.vladivostokcityguide.domain.PlaceCategory.Companion.fromApiValue
import com.example.vladivostokcityguide.domain.models.Landmark
import com.example.vladivostokcityguide.presentation.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlacesScreenViewModel(
    private val repository: CityGuideRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(PlacesScreenState())
    val state: StateFlow<PlacesScreenState> = _state.asStateFlow()

    private val category =
        fromApiValue(savedStateHandle.toRoute<Destination.PlacesScreen>().categoryApi)!!

    init {
        _state.update {
            it.copy(
                selectedCategory = category
            )
        }
        viewModelScope.launch {

            val filterFlow: Flow<Filter> = _state
                .map { it.filter }
                .distinctUntilChanged()

            val orderFlow: Flow<Order> = _state
                .map { it.order }
                .distinctUntilChanged()

            combine(filterFlow, orderFlow) { f, o -> f to o }
                .collect { (f, o) ->
                    filterLandmarks(o, f)
                }
        }

        getLandmarks(category)
    }

    fun onEvent(event: PlacesScreenEvent) {
        when (event) {
            is PlacesScreenEvent.OnFilterChange -> {
                _state.update {
                    it.copy(
                        filter = event.category,
                    )
                }

            }

            is PlacesScreenEvent.OnToggleOrder -> {
                _state.update { it.copy(order = if (it.order == Order.DESCENDING) Order.ASCENDING else Order.DESCENDING) }
            }

            is PlacesScreenEvent.OnToggleSave -> {
                _state.update {
                    it.copy(
                        landmarks = it.landmarks.map { landmark ->
                            if (landmark == event.landmark) {
                                landmark.copy(isSaved = !landmark.isSaved)
                            } else {
                                landmark
                            }
                        }
                    )
                }
                if (!event.landmark.isSaved) {
                    saveLandmark(event.landmark)
                } else {
                    deleteLandmark(event.landmark)
                }
            }

            PlacesScreenEvent.OnRetry -> {
                getLandmarks(category)
            }
        }
    }

    private fun filterLandmarks(order: Order, filter: Filter) {
        return when (filter) {
            Filter.TIME ->
                if (order == Order.ASCENDING)
                    _state.update { it.copy(landmarks = it.landmarks.sortedBy { it.time }) }
                else
                    _state.update { it.copy(landmarks = it.landmarks.sortedByDescending { it.time }) }

            Filter.DISTANCE ->
                if (order == Order.ASCENDING)
                    _state.update { it.copy(landmarks = it.landmarks.sortedBy { it.distance }) }
                else
                    _state.update { it.copy(landmarks = it.landmarks.sortedByDescending { it.distance }) }

            Filter.RATING ->
                if (order == Order.ASCENDING)
                    _state.update { it.copy(landmarks = it.landmarks.sortedBy { it.rating }) }
                else
                    _state.update { it.copy(landmarks = it.landmarks.sortedByDescending { it.rating }) }
            else -> Unit
        }
    }

    private fun getLandmarks(category: PlaceCategory) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = repository.getPlacesByCategory(category)
            when (result) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            landmarks = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
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
