package com.example.vladivostokcityguide.presentation.favorite_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vladivostokcityguide.domain.CityGuideRepository
import com.example.vladivostokcityguide.domain.models.Landmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class FavoriteScreenViewModel(
    private val repository: CityGuideRepository
) : ViewModel() {
    var state by mutableStateOf(FavoriteScreenState())

    init {
        loadFavoriteLandmarks()
    }

    fun onEvent(event: FavoriteScreenEvent) {
        when (event) {
            is FavoriteScreenEvent.DeleteLandmark -> deleteLandmark(event.landmark)
            is FavoriteScreenEvent.OnApplyFilter -> {
                state = state.copy(
                    sortedBy = event.sortField,
                    sortOrder = event.sortOrder,
                    isFilterDialogActive = false
                )
                loadFavoriteLandmarks()
            }
            FavoriteScreenEvent.OnReload -> {
                loadFavoriteLandmarks()
            }
            FavoriteScreenEvent.OnSortOrderChange -> {
                state = state.copy(
                    sortOrder = state.sortOrder.toggle(),

                )
                loadFavoriteLandmarks()
            }
            FavoriteScreenEvent.ToggleFilterDialog -> {
                state = state.copy(isFilterDialogActive = !state.isFilterDialogActive)
            }
        }
    }

    private fun loadFavoriteLandmarks() {
        state = state.copy(isLoading = true, error = null)
        repository.getSortedLandmarks(state.sortedBy.fieldName, state.sortOrder).onEach {
            state = state.copy(
                landmarks = it,
                isLoading = false,
                error = null
            )
        }.launchIn(viewModelScope)
    }

    private fun deleteLandmark(landmark: Landmark) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLandmark(landmark)
        }
    }
}