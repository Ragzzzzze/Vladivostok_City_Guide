package com.example.vladivostokcityguide.app.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)



@SuppressLint("ContextCastToActivity")
@Composable
fun VladivostokCityGuideTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    val activity = LocalContext.current as Activity

    SideEffect {
        activity.window.statusBarColor = Color.Transparent.toArgb()
        WindowCompat.getInsetsController(activity.window, view).isAppearanceLightStatusBars = false
    }

    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
