package com.example.app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app.presentation.googleSignIn.GoogleSignInScreen
import com.example.app.presentation.home.HomeScreen
import com.example.app.presentation.splash.SplashScreen
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PrimaryNavigator(
    navController: NavHostController,
) {
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
                goToSignInScreen = {
                    navController.navigate(NavRoutes.SignInScreen)
                    {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = NavRoutes.HomeScreen,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            HomeScreen()
        }

        composable(
            route = NavRoutes.SignInScreen,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            GoogleSignInScreen(
                moveToNextScreen = {
                    navController.navigate(NavRoutes.HomeScreen)
                    {
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
