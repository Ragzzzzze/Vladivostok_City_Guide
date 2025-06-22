package com.example.vladivostokcityguide.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vladivostokcityguide.presentation.MapScreen.MapScreen
import com.example.vladivostokcityguide.presentation.PlacesScreen.PlacesScreen
import com.example.vladivostokcityguide.presentation.WelcomeScreen.WelcomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Welcome.route
    ) {
        composable(Destination.Welcome.route) {
            WelcomeScreen(
                onCategoryClick = { category ->
                    navController.navigate(Destination.PlacesScreen.createRoute(category))
                }
            )
        }

        composable(Destination.PlacesScreen.route) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            PlacesScreen(
                onStartNavigation = { attractionId ->
                    navController.navigate("map_screen/$attractionId")
                },
                onBackClick = { navController.popBackStack() },
                barText = category,
                initialCategory = category
            )
        }

        composable(Destination.MapScreen.route) { backStackEntry ->
            val attractionId = backStackEntry.arguments?.getString("attractionId")?.toIntOrNull() ?: 0
            MapScreen(
                attractionId = attractionId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}