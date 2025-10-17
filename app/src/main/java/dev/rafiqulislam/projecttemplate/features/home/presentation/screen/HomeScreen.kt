package dev.rafiqulislam.projecttemplate.features.home.presentation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import dev.rafiqulislam.core.base.BaseScreen
import dev.rafiqulislam.projecttemplate.features.home.presentation.viewModels.HomeScreenViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    BaseScreen(
        navController = navController,
        title = "Home",
        viewModel = viewModel,
        showBackButton = false,
        isLoading = false
    ) {
        Text("Welcome to the Home Screen!")
    }

}