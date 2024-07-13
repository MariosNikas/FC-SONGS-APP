package com.example.app.navigation

import TeamSelectionScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app.presentation.VideoSelection.VideoSelectionScreen
import com.example.app.presentation.shared.SharedViewModel
import com.example.app.presentation.splash.SplashScreen
import com.example.app.presentation.video.VideoScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PrimaryNavigator(
    navController: NavHostController,
) {
    val sharedViewModel: SharedViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.SplashScreen,
    ) {
        composable(
            route = NavRoutes.SplashScreen,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            SplashScreen(
                sharedViewModel = sharedViewModel,
                goToNextScreen = {
                    navController.navigate(NavRoutes.TeamSelectionScreen)
                    {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = "${NavRoutes.VideoScreen}/{selectedVideoIndex}",
            arguments = listOf(navArgument("selectedVideoIndex") {
                type = NavType.IntType
                defaultValue = 0
            }),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { backStackEntry ->
            val selectedVideoIndex = backStackEntry.arguments?.getInt("selectedVideoIndex") ?: 0
            VideoScreen(
                selectedVideoIndex = selectedVideoIndex,
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = NavRoutes.TeamSelectionScreen,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            TeamSelectionScreen(
                onNavigateToNextScreen = {
                    navController.navigate(NavRoutes.VideoSelectionScreen)
                },
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = NavRoutes.VideoSelectionScreen,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            VideoSelectionScreen(
                onNavigateToVideoPlayer = { index ->
                    navController.navigate("${NavRoutes.VideoScreen}/$index")
                },
                sharedViewModel = sharedViewModel
            )
        }
    }
}
