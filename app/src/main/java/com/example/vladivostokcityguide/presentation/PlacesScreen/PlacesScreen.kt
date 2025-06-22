package com.example.vladivostokcityguide.presentation.PlacesScreen


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vladivostokcityguide.data.model.Attraction
import com.example.vladivostokcityguide.R
import com.example.vladivostokcityguide.ui.theme.Black
import com.example.vladivostokcityguide.ui.theme.DarkGray80
import com.example.vladivostokcityguide.ui.theme.DarkGray90
import com.example.vladivostokcityguide.ui.theme.DarkerBlack
import com.example.vladivostokcityguide.ui.theme.NeonGreen
import com.example.vladivostokcityguide.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    viewModel: PlacesScreenViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onStartNavigation: (attractionId: Int) -> Unit,
    barText: String = "Place",
    initialCategory: String = ""
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(initialCategory) {
        if (initialCategory.isNotEmpty()) {
            viewModel.selectCategory(initialCategory)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkerBlack)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = barText,
                    color = White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
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
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = White)
            }
        } else if (state.error != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.error}", color = Color.Red)
            }
        } else {
            // Tabs Row
            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                state.categories.forEach { tab ->
                    TabItem(
                        title = tab,
                        isSelected = tab == state.selectedCategory,
                        onClick = { viewModel.selectCategory(tab) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(state.attractions) { attraction ->
                    AttractionCard(
                        attraction = attraction,
                        onStartClick = { onStartNavigation(attraction.id) }
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
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .height(40.dp)
            .shadow(
                elevation = if (isSelected) 4.dp else 2.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Gray.copy(alpha = 0.3f),
                spotColor = Color.Gray.copy(alpha = 0.3f)
            ),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) DarkGray80 else DarkGray90,
        onClick = onClick
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = title,
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AttractionCard(
    attraction: Attraction,
    onStartClick: (Attraction) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = attraction.imageRes),
                contentDescription = attraction.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp)
                    .align(Alignment.BottomStart)
                    .background(color = Black.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = attraction.name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = attraction.distance,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = attraction.time,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Button(
                        onClick = { onStartClick(attraction) },
                        modifier = Modifier
                            .width(78.dp)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonGreen,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.button_start),
                            fontSize = 12.sp,
                            color = Black
                        )
                    }
                }
            }
        }
    }
}