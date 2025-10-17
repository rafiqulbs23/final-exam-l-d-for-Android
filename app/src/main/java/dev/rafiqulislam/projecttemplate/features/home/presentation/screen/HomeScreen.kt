package dev.rafiqulislam.projecttemplate.features.home.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import dev.rafiqulislam.core.base.BaseScreen
import dev.rafiqulislam.projecttemplate.features.home.presentation.viewModels.HomeScreenViewModel
import dev.rafiqulislam.projecttemplate.navigation.TaskListScreenNav

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to the To-Do App!",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    navController.navigate(TaskListScreenNav)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Tasks")
            }
        }
    }

}