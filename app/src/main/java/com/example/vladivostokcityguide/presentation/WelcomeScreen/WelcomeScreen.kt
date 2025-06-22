package com.example.vladivostokcityguide.presentation.WelcomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.vladivostokcityguide.ui.theme.AlgaeBlue
import com.example.vladivostokcityguide.ui.theme.Black
import com.example.vladivostokcityguide.ui.theme.NeonGreen
import com.example.vladivostokcityguide.ui.theme.Purple
import com.example.vladivostokcityguide.ui.theme.SalmonPink
import com.example.vladivostokcityguide.ui.theme.White
import com.example.vladivostokcityguide.R

@Composable
fun WelcomeScreen(
    onCategoryClick: (String) -> Unit
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
            Text(
                text = stringResource(R.string.head_welcome),
                color = White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Light
            )
            Text(
                text = stringResource(R.string.head_city),
                color = White,
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(top = 0.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    CategoryTab(
                        title = stringResource(R.string.category_theater),
                        count = "10 places",
                        color = AlgaeBlue,
                        modifier = Modifier.fillMaxSize()
                            .offset(y = 75.dp),
                        onViewAllClick = { onCategoryClick("Theaters") }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    CategoryTab(
                        title = stringResource(R.string.category_statue),
                        count = "25 places",
                        color = SalmonPink,
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(y = (50).dp),
                        onViewAllClick = { onCategoryClick("Statues") }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    CategoryTab(
                        title = stringResource(R.string.category_museums),
                        count = "16 places",
                        color = Purple,
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(y = (25).dp),
                        onViewAllClick = { onCategoryClick("Museums") }
                    )
                }

                Box(modifier = Modifier.weight(1f)) {
                    CategoryTab(
                        title = stringResource(R.string.category_others),
                        count = "8 places",
                        color = NeonGreen,
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(y = (0).dp),
                        onViewAllClick = { onCategoryClick("Others") }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryTab(
    title: String,
    count: String,
    color: Color,
    modifier: Modifier = Modifier,
    onViewAllClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(color)
            .padding(16.dp)
            .wrapContentSize()
    ) {
        Column(
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Category icon",
                    tint = Black,
                    modifier = Modifier.size(34.dp)
                )

                TextButton(
                    onClick = onViewAllClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Black
                    )
                ) {
                    Text(
                        text = "View All",
                        fontSize = 16.sp
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = Black,
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = count,
                    color = Black,
                    fontSize = 24.sp
                )
            }
        }
    }
}
