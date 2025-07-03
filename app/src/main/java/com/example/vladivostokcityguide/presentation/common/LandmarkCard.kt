package com.example.vladivostokcityguide.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.vladivostokcityguide.R
import com.example.vladivostokcityguide.app.ui.theme.Black
import com.example.vladivostokcityguide.app.ui.theme.NeonGreen
import com.example.vladivostokcityguide.domain.models.Landmark
import com.example.vladivostokcityguide.presentation.UiUtils
import java.util.Locale
import kotlinx.serialization.json.Json

@Composable
fun LandmarkCard(
    landmark: Landmark,
    onClick: (String) -> Unit,
    isSaved: Boolean,
    onToggleSave: (Landmark) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            AsyncImage(
                model = landmark.imgUrl,
                contentDescription = landmark.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .offset(x = -12.dp, y = 15.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable(
                        onClick = { onToggleSave(landmark) }
                    )
                    .align(Alignment.TopEnd)
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center,
            ) {
                    Icon(
                        imageVector = if (!isSaved) Icons.Outlined.BookmarkBorder else Icons.Default.Bookmark,
                        tint = if (!isSaved) Color.White else NeonGreen,
                        contentDescription = "Save landmark"
                    )
            }
            // Rating badge
            Box(
                modifier = Modifier
                    .offset(x = 12.dp, y = 15.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .align(Alignment.TopStart)
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                val rating = landmark.rating
                val ratingColor = when {
                    rating > 4.3 -> NeonGreen
                    rating > 3.0 -> Color.Yellow
                    else -> Color.Red.copy(alpha = 0.8f)
                }
                Text(
                    text = String.format(Locale.getDefault(), "%.1f", rating),
                    color = ratingColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = Black.copy(alpha = 0.6f))
                    .align(Alignment.BottomStart)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 7.dp)
                    ) {
                        Text(
                            text = landmark.name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = UiUtils.formatDistance(landmark.distance),
                                color = Color.White,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "\u2022",
                                color = Color.White,
                                fontSize = 24.sp,
                                modifier = Modifier.padding(horizontal = 7.dp)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.DirectionsWalk,
                                    tint = Color.White,
                                    contentDescription = "walking man",
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = landmark.time.toString() + " min",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val jsonLandmark = Json.encodeToString(landmark)
                            onClick(jsonLandmark)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonGreen
                        ),
                        contentPadding = PaddingValues(16.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.button_start),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W500,
                                color = Black
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowOutward,
                                contentDescription = "View all icon",
                                modifier = Modifier.size(14.dp),
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}