package com.example.vladivostokcityguide.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vladivostokcityguide.presentation.MapScreen.MapScreen
import com.example.vladivostokcityguide.presentation.MapScreen.MapScreenViewModel
import com.example.vladivostokcityguide.presentation.PlacesScreen.PlacesScreen
import com.example.vladivostokcityguide.presentation.PlacesScreen.PlacesScreenViewModel
import com.example.vladivostokcityguide.presentation.WelcomeScreen.WelcomeScreen
import com.example.vladivostokcityguide.presentation.favorite_screen.FavoriteScreen
import com.example.vladivostokcityguide.presentation.favorite_screen.FavoriteScreenViewModel
import com.example.vladivostokcityguide.presentation.landmark_details_screen.LandmarkDetailsScreen
import com.example.vladivostokcityguide.presentation.landmark_details_screen.LandmarkDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.Welcome
    ) {
        composable<Destination.Welcome> {
            WelcomeScreen(
                onCategoryClick = { category ->
                    navController.navigate(Destination.PlacesScreen(category))
                },
                navigateToFavorites = { navController.navigate(Destination.FavoriteScreen) }
            )
        }

        composable<Destination.PlacesScreen> { backStackEntry ->
            val viewModel = koinViewModel<PlacesScreenViewModel>()
            PlacesScreen(
                state = viewModel.state.collectAsState().value,
                onEvent = viewModel::onEvent,
                onBackClick = { navController.navigateUp() },
                navigateToPlaceDetails = {landmark ->
                    navController.navigate(
                        Destination.LandmarkDetailsScreen(landmark)
                    )
                }
            )
        }

        composable<Destination.LandmarkDetailsScreen> {
            val viewModel = koinViewModel<LandmarkDetailsViewModel>()
            LandmarkDetailsScreen(
                state = viewModel.state,
                navController = navController,
                onEvent = viewModel::onEvent,
            )
        }

        composable<Destination.MapScreen> {
            val viewModel = koinViewModel<MapScreenViewModel>()
            MapScreen(
                navController = navController,
                onEvent = viewModel::onEvent,
                state = viewModel.state,
            )
        }
        composable<Destination.FavoriteScreen> {
            val viewModel = koinViewModel<FavoriteScreenViewModel>()
            FavoriteScreen(
                state = viewModel.state,
                onEvent = viewModel::onEvent,
                navigateToDetails = { landmark ->
                    navController.navigate(
                        Destination.LandmarkDetailsScreen(landmark)
                    )
                },
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}