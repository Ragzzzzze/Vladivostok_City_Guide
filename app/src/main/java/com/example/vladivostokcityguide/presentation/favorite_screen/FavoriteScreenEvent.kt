package com.example.vladivostokcityguide.presentation.favorite_screen

import com.example.vladivostokcityguide.domain.Filter
import com.example.vladivostokcityguide.domain.Order
import com.example.vladivostokcityguide.domain.models.Landmark


sealed interface FavoriteScreenEvent {
    data class DeleteLandmark(val landmark: Landmark) : FavoriteScreenEvent
    data object OnReload : FavoriteScreenEvent
    data object ToggleFilterDialog : FavoriteScreenEvent
    data object OnSortOrderChange : FavoriteScreenEvent
    data class OnApplyFilter(val sortField: Filter, val sortOrder: Order) : FavoriteScreenEvent
}
