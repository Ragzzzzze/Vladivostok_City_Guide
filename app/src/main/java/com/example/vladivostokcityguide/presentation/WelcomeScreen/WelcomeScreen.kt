package com.example.vladivostokcityguide.presentation.WelcomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vladivostokcityguide.app.ui.theme.AlgaeBlue
import com.example.vladivostokcityguide.app.ui.theme.Black
import com.example.vladivostokcityguide.app.ui.theme.NeonGreen
import com.example.vladivostokcityguide.app.ui.theme.Purple
import com.example.vladivostokcityguide.app.ui.theme.SalmonPink
import com.example.vladivostokcityguide.app.ui.theme.White
import com.example.vladivostokcityguide.R
import com.example.vladivostokcityguide.domain.PlaceCategory
import com.example.vladivostokcityguide.presentation.WelcomeScreen.components.CategoryTab

@Composable
fun WelcomeScreen(
    onCategoryClick: (String) -> Unit,
    navigateToFavorites: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 60.dp)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    text = stringResource(R.string.head_welcome),
                    color = White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light
                )
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = navigateToFavorites),
                    shape = CircleShape,
                    color = NeonGreen.copy(alpha = 0.3f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Сердечко",
                            tint = NeonGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.head_city) + " 🌊",
                color = White,
                fontSize = 45.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Column() {

                CategoryTab(
                    title = stringResource(R.string.category_theater),
                    count = "10 places",
                    color = AlgaeBlue,
                    modifier = Modifier
                        .offset(y = 62.dp),
                    emoji = "🎭",
                    onClick = { onCategoryClick(PlaceCategory.THEATER.apiValue) }
                )

                CategoryTab(
                    title = stringResource(R.string.category_statue),
                    count = "25 places",
                    color = SalmonPink,
                    emoji = "🗿",
                    modifier = Modifier
                        .offset(y = (42).dp),
                    onClick = { onCategoryClick(PlaceCategory.STATUE.apiValue) }
                )


                CategoryTab(
                    title = stringResource(R.string.category_museums),
                    count = "16 places",
                    color = Purple,
                    emoji = "🏛️",
                    modifier = Modifier
                        .offset(y = (20).dp),
                    onClick = { onCategoryClick(PlaceCategory.MUSEUM.apiValue) }
                )

                CategoryTab(
                    title = stringResource(R.string.category_others),
                    count = "8 places",
                    emoji = "🗺️",
                    color = NeonGreen,
                    onClick = { onCategoryClick(PlaceCategory.OTHER.apiValue) }
                )
            }
        }

    }
}