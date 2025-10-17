package dev.rafiqulislam.core.base

import UiEventHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    modifier: Modifier = Modifier,
    title: String = "",
    viewModel: BaseViewModel,
    navController: NavHostController,
    showBackButton: Boolean = true,
    showBottomBar: Boolean = false,
    topBar: (@Composable () -> Unit)? = null, // Nullable composable lambda
    isLoading: Boolean = false,
    floatingActionButton: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val uiEvent by viewModel.uiEvent.collectAsState()

//    val navController: NavHostController = rememberNavController()
    if(isLoading){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)) // Add semi-transparent black overlay
        ) {
            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.Center)

            ) {
                CircularProgressIndicator()
            }
        }
    }
    else{
        Scaffold(
            topBar = {
                // Define the top bar composable inline
                when {
                    topBar != null -> {
                        topBar() // Invoke the provided topBar composable
                    }

                    title.isNotEmpty() -> {
                        TopAppBar(
                            title = { Text(text = title) },
                            navigationIcon = {
                                if (showBackButton) {
                                    IconButton(onClick = {
                                        navController.popBackStack()
                                    }) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "Back",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.width(0.dp))
                                }
                            },
//                        colors = TopAppBarDefaults.topAppBarColors(
//                            containerColor = MaterialTheme.colorScheme.surface,
//                            scrolledContainerColor = MaterialTheme.colorScheme.surface
//                        )
                        )
                    }

                    else -> {
                        // Empty composable for no top bar
                    }
                }
            },
            modifier = modifier,
            floatingActionButton = {
                floatingActionButton?.invoke()
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                content(padding)
                // Loading Indicator




                // UI Event Handler
                UiEventHandler(
                    uiEvent = uiEvent,
                    onEventConsumed = { viewModel.clearUiEvent() },
                    onSnackbarAction = {

                    },
                    onErrorRetry = {
                    },
                    popBackstack = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
        }
    }

}
