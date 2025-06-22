package com.example.vladivostokcityguide

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vladivostokcityguide.presentation.MapScreen.RequestLocationPermission
import com.example.vladivostokcityguide.presentation.navigation.AppNavigation
import com.example.vladivostokcityguide.ui.theme.VladivostokCityGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VladivostokCityGuideTheme {
                RequestLocationPermission()
                AppNavigation()
            }
        }
    }
}