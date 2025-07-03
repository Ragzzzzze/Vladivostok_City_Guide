package com.example.vladivostokcityguide.presentation.MapScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.vladivostokcityguide.app.ui.theme.NeonGreen
import com.example.vladivostokcityguide.app.ui.theme.Purple
import com.example.vladivostokcityguide.app.ui.theme.VkBlue
import com.example.vladivostokcityguide.app.ui.theme.VladivostokCityGuideTheme
import com.example.vladivostokcityguide.app.ui.theme.White
import com.example.vladivostokcityguide.domain.models.Landmark
import com.example.vladivostokcityguide.domain.models.Route
import com.example.vladivostokcityguide.presentation.MapScreen.SelectedLandmark
import com.example.vladivostokcityguide.presentation.UiUtils
import com.example.vladivostokcityguide.presentation.landmark_details_screen.AmbientContainer
import kotlinx.serialization.json.Json

@Composable
fun LandmarkBottomSheetContent(
    selectedLandmark: SelectedLandmark,
    onDismiss: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onToggleShowRoute: () -> Unit,
    isLandmarkSaved: Boolean,
    onToggleSave: () -> Unit
) {
    val landmark = selectedLandmark.landmark
    val isRouteFetched = selectedLandmark.route != null
    val isShowRoute = selectedLandmark.isShowRoute
    Column(modifier = Modifier.systemBarsPadding()) {
        AsyncImage(
            model = landmark.imgUrl,
            contentDescription = "landmark image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(bottom = 15.dp)
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = landmark.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.7f))
                        .clickable { onDismiss() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Закрыть",
                        tint = Color.Black,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
            Text(
                text = landmark.type,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(vertical = 3.dp),
                color = Color.Gray
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = landmark.rating.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        modifier = Modifier.padding(end = 4.dp)
                    )

                    val rating = landmark.rating
                    val starSize = 14.dp

                    for (i in 1..5) {
                        when {
                            i <= rating -> {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = "Star Icon",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(starSize)
                                )
                            }

                            i - 0.5 <= rating -> {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.StarHalf,
                                    contentDescription = "Star Icon",
                                    tint = Color.Yellow,
                                    modifier = Modifier.size(starSize)
                                )
                            }

                            else -> {
                                Icon(
                                    imageVector = Icons.Filled.StarOutline,
                                    contentDescription = "Star Icon",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(starSize)
                                )
                            }
                        }
                    }
                }
                AmbientContainer(
                    paddingValues = PaddingValues(
                        vertical = 5.dp,
                        horizontal = 7.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Route,
                        contentDescription = "Distance icon",
                        tint = NeonGreen,
                        modifier = Modifier.padding(end = 3.dp)
                    )
                    if (selectedLandmark.route == null) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = White,
                        )
                    } else {
                        Text(
                            text = UiUtils.formatDistance(selectedLandmark.route.distance),
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Time icon",
                        tint = NeonGreen,
                        modifier = Modifier.padding(end = 3.dp)
                    )
                    if (selectedLandmark.route == null) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = White,
                        )
                    } else {
                        Text(
                            text = selectedLandmark.route.timeMin.toString() + " min",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }

            }
            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(44.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick = onToggleShowRoute,
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isShowRoute) VkBlue else NeonGreen,
                        contentColor = if (!isShowRoute) Color.White else Color.Black
                    ),
                    enabled = isRouteFetched,
                ) {
                    if (!isRouteFetched) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.Black,
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Outlined.Route,
                            contentDescription = "Distance icon",
                            modifier = Modifier.padding(end = 3.dp)
                        )
                    }
                    Text(
                        text = if (!isShowRoute) "Маршрут" else "Завершить",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                IconButton(
                    onClick = { onToggleSave() },
                    modifier = Modifier
                        .fillMaxHeight()
                        .background(
                            Color.DarkGray.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp)
                ) {
                    Icon(
                        imageVector = if (!isLandmarkSaved) Icons.Outlined.BookmarkBorder else Icons.Filled.Bookmark,
                        contentDescription = "Save landmark",
                        tint = if (!isLandmarkSaved) NeonGreen else Color.Yellow,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        val jsonLandmark = Json.encodeToString(landmark)
                        onNavigateToDetails(jsonLandmark)
                    },
                    modifier = Modifier.fillMaxHeight(),
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Purple,
                        contentColor = Color.Black
                    ),
                ) {
                    Text(
                        text = "Подробнее",
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowOutward,
                        contentDescription = "Details icon",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandmarkBottomSheetScaffold(
    selectedLandmark: SelectedLandmark?,
    onDismiss: () -> Unit,
    onNavigateToDetails: (String) -> Unit,
    onToggleShowRoute: () -> Unit,
    isLandmarkSaved: Boolean,
    onToggleSave: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 60.dp,
        sheetSwipeEnabled = true,
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                Modifier.padding(vertical = 8.dp)
            )
        },
        sheetContent = {
            if (selectedLandmark != null) {
                LandmarkBottomSheetContent(
                    selectedLandmark,
                    onDismiss,
                    onNavigateToDetails,
                    onToggleShowRoute,
                    isLandmarkSaved,
                    onToggleSave
                )
            }else {
                Box(Modifier.height(1.dp))
            }
        },
        content = content
    )
}

@Preview
@Composable
fun LandmarkBottomSheetScaffoldPreview() {
    val landmark = Landmark(
        name = "Русский мост",
        latitude = 43.0639,
        longitude = 131.9058,
        description = "Вантовый мост во Владивостоке, соединяющий остров Русский с материковой частью города.",
        imgUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/Russky_Bridge_%28October_2024%29-0_2.jpg/500px-Russky_Bridge_%28October_2024%29-0_2.jpg",
        rating = 4.8,
        facts = emptyList(),
        time = 15,
        distance = 200,
        isSaved = false,
        type = "Мост"
    )
    val route = Route(
        distance = 1234,
        timeMin = 15,
        coordinates = emptyList(),
    )
    val selectedLandmark = SelectedLandmark(
        landmark = landmark,
        route = route
    )
    Column(Modifier.fillMaxSize()) {
        VladivostokCityGuideTheme {
            Surface {
                LandmarkBottomSheetScaffold(
                    selectedLandmark = selectedLandmark,
                    onDismiss = {},
                    onNavigateToDetails = {},
                    onToggleShowRoute = {},
                    isLandmarkSaved = false,
                    onToggleSave = {}
                ) { paddingValues ->
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        Text("Main content (e.g. Map) goes here")
                    }
                }
            }
        }
    }
}
