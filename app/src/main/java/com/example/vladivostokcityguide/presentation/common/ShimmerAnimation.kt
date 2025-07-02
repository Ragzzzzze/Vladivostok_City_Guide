package com.example.vladivostokcityguide.presentation.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun ShimmerAnimation(
    modifier: Modifier = Modifier,
    shimmerColor: Color = Color.LightGray.copy(alpha = 0.6f),
    backgroundColor: Color = Color.LightGray.copy(alpha = 0.2f),
    durationMillis: Int = 1000
) {
    val transition = rememberInfiniteTransition(label = "shimmer_transition")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ), label = "shimmer_translate_anim"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            backgroundColor,
            shimmerColor,
            backgroundColor,
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim.value * 500f, y = translateAnim.value * 500f) // Adjust this for shimmer speed/angle
    )

    Box(
        modifier = modifier
            .background(brush)
    )
}

@Composable
fun ShimmerItem(modifier: Modifier = Modifier) {
    ShimmerAnimation(modifier = modifier)
} 