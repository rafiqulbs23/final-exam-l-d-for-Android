import dev.rafiqulislam.core.data.SnackBarType

import androidx.compose.ui.Alignment
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import dev.rafiqulislam.core.base.UiEvent


@Composable
fun UiEventHandler(
    modifier: Modifier = Modifier,
    uiEvent: UiEvent?,
    onEventConsumed: () -> Unit,
    onSnackbarAction: () -> Unit = {},
    onErrorRetry: () -> Unit = {},
    popBackstack: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showErrorDialog by remember { mutableStateOf(false) }
    var showWarningDialog by remember { mutableStateOf(false) }

    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var isPopBackStack by remember { mutableStateOf(false) }

    var canRetry by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiEvent) {
        when (uiEvent) {
            is UiEvent.ShowSnackbar -> {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message,
                        actionLabel = uiEvent.actionLabel,
                        duration = uiEvent.duration,
                    )
                    onEventConsumed()
                }
            }
            is UiEvent.ShowErrorMessage -> {
               /* dialogMessage = uiEvent.message
                canRetry = uiEvent.canRetry
                showErrorDialog = true
                onEventConsumed()*/
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message,
                        actionLabel = uiEvent.actionLabel,
                        duration = uiEvent.duration,
                    ).also {
                        if (uiEvent.canRetry) {
                            onErrorRetry()
                        }
                    }
                    onEventConsumed()
                }
            }
            is UiEvent.ShowWarningMessage -> {
               /* dialogMessage = uiEvent.message
                showWarningDialog = true
                onEventConsumed()*/
                scope.launch {
                    snackbarHostState.showSnackbar(
                        message = uiEvent.message,
                        actionLabel = uiEvent.actionLabel,
                        duration = uiEvent.duration,
                    )
                    onEventConsumed()
                }
            }

            is UiEvent.ShowErrorDialog -> {
                dialogMessage = uiEvent.message
                canRetry = uiEvent.canRetry
                showErrorDialog = true
                onEventConsumed()
            }

            is UiEvent.ShowSuccessDialog -> {
                dialogTitle = uiEvent.title
                dialogMessage = uiEvent.message
                isPopBackStack = uiEvent.isPopBackstack
                showSuccessDialog = true
                onEventConsumed()
            }

            null -> {}
        }
    }

    // Snackbar Host

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            val type = (uiEvent as? UiEvent.ShowSnackbar)?.type ?: SnackBarType.DEFAULT
//            val (containerColor, contentColor) = when (type) {
//                SnackBarType.DEFAULT -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
//                SnackBarType.ERROR -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
//                SnackBarType.WARNING -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
//            }

            Snackbar(
//                containerColor = containerColor,
//                contentColor = contentColor,
//                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(16.dp),
                action = {
                    snackbarData.visuals.actionLabel?.let {
                        TextButton(onClick = {
                            onSnackbarAction()
                            snackbarData.dismiss()
                        }) {
                            Text(it)
                        }
                    }
                },
                content = { Text(snackbarData.visuals.message) }
            )
        }
    )


    // Error Dialog
    AnimatedVisibility(
        visible = showErrorDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AlertDialog(
            icon = { Icon(imageVector = Icons.Default.ErrorOutline, contentDescription = "Warning Icon") },
//            iconContentColor = MaterialTheme.colorScheme.tertiary,
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error", style = MaterialTheme.typography.titleLarge) },
            text = { Text(dialogMessage, style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                if (canRetry) {
                    TextButton(onClick = {
                        onErrorRetry()
                        showErrorDialog = false
                    }) {
                        Text("Retry")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("Dismiss")
                }
            }
        )
    }

    // Warning Dialog
    AnimatedVisibility(
        visible = showWarningDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AlertDialog(
            icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = "Warning Icon") },
//            iconContentColor = MaterialTheme.colorScheme.tertiary,
            onDismissRequest = { showWarningDialog = false },
            title = { Text("Warning", style = MaterialTheme.typography.titleLarge) },
            text = { Text(dialogMessage, style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                TextButton(onClick = { showWarningDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    // Success Dialog
    AnimatedVisibility(
        visible = showSuccessDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AlertDialog(
            icon = { Icon(imageVector = Icons.Outlined.CheckCircleOutline, contentDescription = "Success Icon") },
            iconContentColor = MaterialTheme.colorScheme.secondary,
            onDismissRequest = { showSuccessDialog = false },
            title = { Text(dialogTitle, style = MaterialTheme.typography.titleLarge) },
            text = { Text(dialogMessage, style = MaterialTheme.typography.bodyMedium) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSuccessDialog = false
                        if(isPopBackStack) popBackstack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }


}

