package com.example.vladivostokcityguide.presentation.PlacesScreen


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vladivostokcityguide.app.ui.theme.Black
import com.example.vladivostokcityguide.app.ui.theme.DarkGray80
import com.example.vladivostokcityguide.app.ui.theme.DarkGray90
import com.example.vladivostokcityguide.app.ui.theme.DarkerBlack
import com.example.vladivostokcityguide.app.ui.theme.NeonGreen
import com.example.vladivostokcityguide.app.ui.theme.White
import com.example.vladivostokcityguide.domain.Filter
import com.example.vladivostokcityguide.domain.Order
import com.example.vladivostokcityguide.presentation.MapScreen.MapScreen
import com.example.vladivostokcityguide.presentation.common.ErrorDialog
import com.example.vladivostokcityguide.presentation.common.LandmarkCard
import com.example.vladivostokcityguide.presentation.common.ShimmerLandmark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    state: PlacesScreenState,
    onEvent: (PlacesScreenEvent) -> Unit,
    onBackClick: () -> Unit = {},
    navigateToPlaceDetails: (landmark: String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkerBlack)
    ) {
        TopAppBar(
            title = {
                state.selectedCategory?.let {
                    Text(
                        text = stringResource(it.nameId),
                        color = White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkerBlack
            )
        )

        if (state.isLoading) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(count = 5) {
                    ShimmerLandmark()
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        } else if (state.error != null) {
            ErrorDialog(
                message = state.error,
                onRetry = { onEvent(PlacesScreenEvent.OnRetry) },
            )
        } else {
            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Order
                val order = state.order
                TabItem(
                    title = stringResource(order.resName),
                    isSelected = true,
                    onClick = { onEvent(PlacesScreenEvent.OnToggleOrder) },
                    icon = if (order == Order.DESCENDING) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentColor = Black,
                    background = NeonGreen.copy(alpha = 0.3f),
                    selectedBackgroundColor = NeonGreen
                )
                Spacer(modifier = Modifier.width(8.dp))
                //Filter
                Filter.entries.filterNot { it == Filter.SAVING_DATE }.forEach { filter ->
                    TabItem(
                        title = stringResource(filter.labelRes),
                        isSelected = filter == state.filter,
                        onClick = { onEvent(PlacesScreenEvent.OnFilterChange(filter)) },
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(state.landmarks) { landmark ->
                    LandmarkCard(
                        landmark = landmark,
                        onClick = { landmark ->
                            navigateToPlaceDetails(landmark)
                        },
                        isSaved = landmark.isSaved,
                        onToggleSave = { onEvent(PlacesScreenEvent.OnToggleSave(it)) },
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}


@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector? = null,
    contentColor: Color = Color.White,
    background: Color = Color.Gray.copy(alpha = 0.3f),
    selectedBackgroundColor: Color = DarkGray80,
    unselectedBackgroundColor: Color = DarkGray90
) {
    Surface(
        modifier = Modifier
            .height(40.dp)
            .shadow(
                elevation = if (isSelected) 4.dp else 2.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = background,
                spotColor = background
            ),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) selectedBackgroundColor else unselectedBackgroundColor,
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                icon?.let {
                    Icon(
                        imageVector = icon,
                        contentDescription = "icon",
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 3.dp),
                        tint = contentColor
                    )
                }

                Text(
                    text = title,
                    color = if (isSelected) contentColor else contentColor.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }

        }
    }
}
