package com.example.vladivostokcityguide.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.example.vladivostokcityguide.presentation.navigation.AppNavigation
import com.example.vladivostokcityguide.app.ui.theme.VladivostokCityGuideTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VladivostokCityGuideTheme {
                Surface {
                    RequestLocationPermission()
                    AppNavigation()
                }
            }
        }
    }
}