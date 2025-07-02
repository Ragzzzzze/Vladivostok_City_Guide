package com.example.vladivostokcityguide.presentation.landmark_details_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale.Companion.Crop
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.vladivostokcityguide.app.ui.theme.NeonGreen
import com.example.vladivostokcityguide.app.ui.theme.White
import com.example.vladivostokcityguide.presentation.UiUtils
import com.example.vladivostokcityguide.presentation.favorite_screen.FavoriteScreenEvent
import com.example.vladivostokcityguide.presentation.navigation.Destination
import kotlinx.serialization.json.Json

@Composable
fun LandmarkDetailsScreen(
    state: LandmarkDetailsScreenState,
    onEvent: (LandmarkDetailsScreenEvent) -> Unit,
    navController: NavController
) {
    val scrollState = rememberScrollState()
    state.landmark?.let { landmark ->
        Column(
            modifier = Modifier.verticalScroll(scrollState).navigationBarsPadding()
        ) {
            Box() {
                AsyncImage(
                    model = landmark.imgUrl,
                    contentDescription = "landmark image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = Crop,
                    alignment = Alignment.TopCenter,
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .align(Alignment.TopCenter)
                        .padding(8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AmbientContainer(
                            modifier = Modifier
                                .padding(end = 40.dp)
                                .weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location Icon",
                                tint = NeonGreen
                            )
                            Text(
                                text = landmark.name,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 2,
                            )
                        }
                        Row {
                            AmbientContainer(
                                enabled = state.route != null,
                                onClick = {
                                    val landmarkJson = Json.encodeToString(landmark)
                                    val routeJson = Json.encodeToString(state.route)
                                    navController.navigate(
                                        Destination.MapScreen(
                                            landmarkJson = landmarkJson,
                                            routeJson = routeJson
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Explore,
                                    contentDescription = "Explore icon",
                                    tint = NeonGreen,
                                )
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            AmbientContainer(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Time icon",
                                    tint = Color.White,
                                )
                            }
                        }

                    }

                    Spacer(modifier = Modifier.height(7.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        AmbientContainer {
                            Icon(
                                imageVector = Icons.Outlined.Route,
                                contentDescription = "Distance icon",
                                tint = NeonGreen,
                                modifier = Modifier.padding(end = 3.dp)
                            )
                            if (state.route == null) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = White,
                                )
                            } else {
                                Text(
                                    text = UiUtils.formatDistance(state.route.distance),
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
                            if (state.route == null) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = White,
                                )
                            } else {
                                Text(
                                    text = state.route.timeMin.toString() + " min",
                                    color = Color.White,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable(
                                    onClick = { onEvent(LandmarkDetailsScreenEvent.ToggleSave) }
                                )
                                .background(Color.DarkGray.copy(alpha = 0.8f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = if (!state.landmark.isSaved) Icons.Outlined.BookmarkBorder else Icons.Default.Bookmark,
                                tint = if (!state.landmark.isSaved) Color.White else NeonGreen,
                                contentDescription = "Save landmark"
                            )
                        }
                    }

                }
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "📍 " + landmark.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )
                Text(
                    text = "Описание",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                )
                Text(
                    text = landmark.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.6f)
                )
                Text(
                    text = "Факты",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(top = 16.dp, bottom = 12.dp)
                )
                Row {
                    LazyRow(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = spacedBy(15.dp),
                    ) {
                        items(landmark.facts) { fact ->
                            FactCard(
                                fact = fact,
                                modifier = Modifier
                                    .width(200.dp)
                                    .height(150.dp)
                                    .weight(1f),
                                onClick = {
                                    onEvent(
                                        LandmarkDetailsScreenEvent.ShowFact(fact)
                                    )
                                }

                            )
                        }
                    }
                }
            }
        }
        if (state.factToShow != null) {
            FactCardDialog(
                fact = state.factToShow,
                onDismiss = { onEvent(LandmarkDetailsScreenEvent.CloseFact) }
            )
        }
    }
}

@Composable
fun FactCardDialog(
    fact: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(10.dp),
        ) {
            Column() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть"
                        )
                    }
                }

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.DarkGray)
                ) {
                    Box(Modifier.padding(10.dp)) {
                        Column {
                            Text(
                                text = "Факт",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                            )
                            Text(
                                text = fact,
                                style = MaterialTheme.typography.bodyMedium,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AmbientContainer(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit) = { },
    paddingValues: PaddingValues = PaddingValues(13.dp),
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    Surface(
        color = Color.DarkGray.copy(alpha = 0.8f),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier,
        onClick = onClick,
        enabled = enabled
    ) {
        Box(modifier = Modifier.padding(paddingValues)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                content()
            }
        }
    }
}

@Composable
fun FactCard(
    fact: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color.DarkGray)
            .clickable(onClick = { onClick() })
    ) {
        Box(Modifier.padding(10.dp)) {
            Column {
                Text(
                    text = "Факт",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White,
                )
                Text(
                    text = fact,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}