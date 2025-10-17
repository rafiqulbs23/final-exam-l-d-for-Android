package dev.rafiqulislam.core.base

import androidx.compose.material3.SnackbarDuration
import dev.rafiqulislam.core.data.SnackBarType


sealed class UiEvent {
    /**
     * Shows a snackbar with a message and optional action.
     */
    data class ShowSnackbar(
        val message: String,
        val actionLabel: String? = null,
        val duration: SnackbarDuration = SnackbarDuration.Short,
        val type: SnackBarType = SnackBarType.DEFAULT
    ) : UiEvent()

    /**
     * Shows an error dialog with a message and optional retry action.
     */
    data class ShowErrorMessage(
        val message: String,
        val canRetry: Boolean = false,
        val actionLabel: String = "Retry",
        val duration: SnackbarDuration = SnackbarDuration.Short,
        ) : UiEvent()

    /**
     * Shows a warning dialog with a message.
     */
    data class ShowWarningMessage(
        val message: String,
        val actionLabel: String = "",
        val duration: SnackbarDuration = SnackbarDuration.Short,
        ) : UiEvent()

    data class ShowErrorDialog(
        val message: String,
        val canRetry: Boolean = false,
        val actionLabel: String = "Retry",
    ): UiEvent()

    data class ShowSuccessDialog(
        val title: String = "Success",
        val message: String = "Operation completed successfully.",
        val isPopBackstack: Boolean = false,
    ): UiEvent()
}