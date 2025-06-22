package com.example.vladivostokcityguide.presentation.PlacesScreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vladivostokcityguide.data.repository.AttractionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlacesScreenViewModel(
    private val repository: AttractionsRepository = AttractionsRepository()
) : ViewModel() {
    private val _state = MutableStateFlow(PlacesScreenState())
    val state: StateFlow<PlacesScreenState> = _state.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val categories = repository.getCategories()
                _state.update {
                    it.copy(
                        categories = categories,
                        selectedCategory = categories.firstOrNull() ?: "",
                        isLoading = false
                    )
                }
                loadAttractions()
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun selectCategory(category: String) {
        _state.update { it.copy(selectedCategory = category) }
        loadAttractions()
    }

    private fun loadAttractions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val attractions = repository.getAttractionsByCategory(state.value.selectedCategory)
                _state.update {
                    it.copy(
                        attractions = attractions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }
}