package com.example.vladivostokcityguide.presentation.favorite_screen

import com.example.vladivostokcityguide.domain.Filter
import com.example.vladivostokcityguide.domain.Order
import com.example.vladivostokcityguide.domain.models.Landmark

data class FavoriteScreenState(
    val landmarks: List<Landmark> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFilterDialogActive: Boolean = false,
    val sortedBy: Filter = Filter.SAVING_DATE,
    val sortOrder: Order = Order.DESCENDING,
)
