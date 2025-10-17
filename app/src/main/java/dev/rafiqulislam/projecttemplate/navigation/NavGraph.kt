package dev.rafiqulislam.projecttemplate.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.rafiqulislam.projecttemplate.features.home.presentation.screen.HomeScreen
import dev.rafiqulislam.projecttemplate.features.splash.presentation.screen.SplashScreen
import dev.rafiqulislam.projecttemplate.features.tasks.presentation.screen.AddEditTaskScreen
import dev.rafiqulislam.projecttemplate.features.tasks.presentation.screen.TaskListScreen
import dev.rafiqulislam.projecttemplate.features.tasks.presentation.viewModels.AddEditTaskViewModel
import dev.rafiqulislam.projecttemplate.features.tasks.presentation.viewModels.TaskListViewModel



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

            composable<HomeScreenNav> { 
                HomeScreen(navController) 
            }

            composable<TaskListScreenNav> {
                val viewModel: TaskListViewModel = hiltViewModel()
                TaskListScreen(
                    viewModel = viewModel,
                    onNavigateToAddTask = {
                        navController.navigate(AddTaskScreenNav())
                    },
                    onNavigateToEditTask = { taskId ->
                        navController.navigate(EditTaskScreenNav(taskId))
                    }
                )
            }

            composable<AddTaskScreenNav> { backStackEntry ->
                val viewModel: AddEditTaskViewModel = hiltViewModel()
                AddEditTaskScreen(
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable<EditTaskScreenNav> { backStackEntry ->
                val taskId = backStackEntry.toRoute<EditTaskScreenNav>().taskId
                val viewModel: AddEditTaskViewModel = hiltViewModel()
                AddEditTaskScreen(
                    viewModel = viewModel,
                    taskId = taskId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

        }

    }
}

