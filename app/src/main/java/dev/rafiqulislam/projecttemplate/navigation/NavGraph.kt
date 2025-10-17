package dev.rafiqulislam.projecttemplate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.rafiqulislam.projecttemplate.features.home.presentation.screen.HomeScreen
import dev.rafiqulislam.projecttemplate.features.splash.presentation.screen.SplashScreen



@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
) {

    CompositionLocalProvider {
        NavHost(
            navController = navController,
            startDestination = SplashScreenNav,
//            startDestination = PrescriptionSurveyScreenNav

        ) {
            composable<SplashScreenNav> {
                SplashScreen(navController = navController)
            }

            composable<HomeScreenNav> { HomeScreen(navController) }

        }

    }
}

