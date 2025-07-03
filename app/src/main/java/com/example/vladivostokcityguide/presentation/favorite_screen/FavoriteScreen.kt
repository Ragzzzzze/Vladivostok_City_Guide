package com.example.vladivostokcityguide.presentation.favorite_screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.vladivostokcityguide.app.ui.theme.NeonGreen
import com.example.vladivostokcityguide.domain.Filter
import com.example.vladivostokcityguide.domain.Order
import com.example.vladivostokcityguide.presentation.common.ErrorDialog
import com.example.vladivostokcityguide.presentation.common.LandmarkCard
import com.example.vladivostokcityguide.presentation.common.ShimmerLandmark
import com.example.vladivostokcityguide.presentation.favorite_screen.components.FilterDialog
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    state: FavoriteScreenState,
    onEvent: (FavoriteScreenEvent) -> Unit,
    navigateToDetails: (String) -> Unit,
    navigateBack: () -> Unit
) {
    if (state.isFilterDialogActive) {
        FilterDialog(
            onDismissRequest = { onEvent(FavoriteScreenEvent.ToggleFilterDialog) },
            onApply = { sortField, sortOrder ->
                onEvent(FavoriteScreenEvent.OnApplyFilter(sortField, sortOrder))
            },
            initialSortField = state.sortedBy,
            initialSortOrder = state.sortOrder,
            availableSortFields = Filter.entries
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Избранное") },
                navigationIcon = {
                    IconButton(onClick = navigateBack ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },

    ) { innerPadding ->
        when {
            state.isLoading -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    items(count = 5) {
                        ShimmerLandmark()
                    }
                }
            }

            state.error != null -> {
                ErrorDialog(
                    state.error
                ) {
                    onEvent(FavoriteScreenEvent.OnReload)
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .padding(top = 11.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { onEvent(FavoriteScreenEvent.ToggleFilterDialog) },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(45.dp)
                                    .background(Color.DarkGray)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FilterAlt,
                                    tint = Color.White,
                                    contentDescription = "Filter Icon",
                                )

                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(
                                modifier = Modifier.clickable { onEvent(FavoriteScreenEvent.OnSortOrderChange) },
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = stringResource(state.sortedBy.labelRes),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = NeonGreen,
                                    modifier = Modifier
                                )
                                val sortIcon = when (state.sortOrder) {
                                    Order.ASCENDING -> Icons.Default.ArrowUpward
                                    Order.DESCENDING -> Icons.Default.ArrowDownward
                                }
                                Icon(
                                    imageVector = sortIcon,
                                    contentDescription = "Sort Icon",
                                    tint = NeonGreen,
                                    modifier = Modifier.height(14.dp)
                                )
                            }
                        }

                    }

                    items(state.landmarks) { landmark ->
                        LandmarkCard(
                            landmark = landmark,
                            onClick = {
                                val jsonLandmark = Json.encodeToString(landmark)
                                navigateToDetails(jsonLandmark)
                            },
                            isSaved = landmark.isSaved,
                            onToggleSave = { onEvent(FavoriteScreenEvent.DeleteLandmark(it)) }
                        )
                    }
                }
            }

        }
    }
}